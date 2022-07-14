package bkav.com.springboot.services.impl;

import bkav.com.springboot.helper.ConnectDB;
import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.Category;
import bkav.com.springboot.models.Entities.CategoryFieldName;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.models.Mapper.MapCategory;
import bkav.com.springboot.payload.request.CategoryInsertRequest;
import bkav.com.springboot.payload.request.CategoryRequest;
import bkav.com.springboot.payload.response.CategoryResponse;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.response.PageInfo;
import bkav.com.springboot.payload.util.*;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.CategoryFieldNameRepository;
import bkav.com.springboot.repository.CategoryRepository;
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.CategoryService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Value("${spring.datasource.username}")
    private String userDB;

    @Value("${spring.datasource.password}")
    private String passDB;

    @Value("${spring.datasource.url}")
    private String urlDB;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryFieldNameRepository categoryFieldNameRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileServiceImpl fileService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Category> categories = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            org.springframework.data.domain.Page<Category> pageCategory;
            if (name == null) pageCategory = categoryRepository.findAll(paging);
            else pageCategory = categoryRepository.findAllByCatTableNameIsContaining(name, paging);
            categories = pageCategory.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("pages", categories);
            response.put("currentPage", (pageCategory.getNumber() + 1));
            response.put("totalItems", categories.size());
            response.put("totalPages", categoryRepository.findAll().size());
            logger.info("get list category success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_CATEGORY);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_CATEGORY);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, response, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> getListNewCreate(String ip) {
        try {
            List<Category> categories = categoryRepository.findTop5ByOrderByCreateTimeDesc();
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, categories, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> getListNewUpdate(String ip) {
        try {
            List<Category> categories = categoryRepository.findTop5ByOrderByUpdateTimeDesc();
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, categories, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> create(CategoryRequest categoryRequest, String ip) {
        try {
            logger.info("categoryRequest: " + categoryRequest);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String tableName = VNCharacterUtils.removeAccent(categoryRequest.getNameCategory()).trim().replaceAll("\\s+", "_").toLowerCase();
            Category category = MapCategory.getCategory(tableName, categoryRequest.getNameCategory(), categoryRequest.isTypeCategory(), userDetails.getUsername());
            categoryRepository.save(category);
            Optional<Category> categoryResult = categoryRepository.findByCatTableName(tableName);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("CREATE TABLE `");
            strBuilder.append(tableName);
            strBuilder.append("`( `id` int(100) NOT NULL AUTO_INCREMENT,");
            if (categoryRequest.getFields().size() > 0) {
                for (Field field : categoryRequest.getFields()) {
                    strBuilder.append("`");
                    String fieldName = VNCharacterUtils.removeAccent(field.getNameField()).trim().replaceAll("\\s+", "_").toLowerCase();
                    String dataType = field.getTypeField();
                    String defaultValue = field.getDefaultValue();
                    boolean isNull = field.isNull();
                    CategoryFieldName categoryFieldName = MapCategory.getCategoryFieldName(categoryResult.get().getId(), fieldName, field.getNameField(), dataType, defaultValue, isNull, userDetails.getUsername());
                    categoryFieldNameRepository.save(categoryFieldName);
                    strBuilder.append(fieldName);
                    strBuilder.append("` ");
                    if (dataType.equals("VARCHAR")) {
                        strBuilder.append("VARCHAR(");
                        strBuilder.append(field.getSizeValue());
                        strBuilder.append(") CHARACTER SET utf8mb4 ");
                    } else if (dataType.equals("BIGINT")) {
                        strBuilder.append("BIGINT(20) ");
                    } else {
                        strBuilder.append(" DATETIME ");
                    }
                    strBuilder.append("DEFAULT ");
                    if (defaultValue != null && !defaultValue.equals("")) {
                        strBuilder.append("'");
                        strBuilder.append(defaultValue);
                        strBuilder.append("'");
                    } else {
                        strBuilder.append("NULL");
                    }
                    strBuilder.append(",");
                }
                if (categoryRequest.isTypeCategory() == true) {
                    strBuilder.append("`level` VARCHAR(5) DEFAULT '0', ");
                }
                strBuilder.append("`status` int(5) DEFAULT 1, ");
                strBuilder.append("`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP, ");
                strBuilder.append("`modify_time` DATETIME DEFAULT CURRENT_TIMESTAMP, ");
                strBuilder.append("PRIMARY KEY (`id`) USING BTREE");
                strBuilder.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
                categoryRepository.createCategory(strBuilder.toString());
            }
            logger.info("create category success");
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CREATE_CATEGORY);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.CREATE_CATEGORY);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.CREATE_CATEGORY, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> insert(CategoryInsertRequest categoryInsertRequest, String ip) {
        try {
            StringBuilder listColumn = new StringBuilder();
            StringBuilder listValue = new StringBuilder();
            Iterator var3 = categoryInsertRequest.getLsData().iterator();

            while (var3.hasNext()) {
                LsData data = (LsData) var3.next();
                if (data.getValue().trim() != null) {
                    {
                        listColumn.append("`");
                        listColumn.append(data.getColumn());
                        listColumn.append("`,");
                        listValue.append("'");
                        listValue.append(data.getValue());
                        listValue.append("',");
                    }
                }
            }
            String lsColumn = listColumn.toString();
            lsColumn = lsColumn.substring(0, lsColumn.length() - 1);
            String lsValue = listValue.toString();
            lsValue = lsValue.substring(0, lsValue.length() - 1);
            logger.info("lsColumn: " + lsColumn);
            logger.info("lsValue: " + lsValue);
            int code = categoryRepository.insertCategory(categoryInsertRequest.getIdCategory(), lsColumn, lsValue);
            logger.info("code: " + code);
            if (code == 0) {
                logger.info("insert category success");
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.INSERT_CATEGORY);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_READ);
                    activityLog.setContent(Content.ADMIN + " " + Content.INSERT_CATEGORY);
                }
                activityLog.setUsername(userDetails.getUsername());
                activityLog.setIp(ip);
                activityLog.setUserId(user.get().getId());
                activityLogRepository.save(activityLog);
                logger.info("insert success activity log");
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.INSERT_SUCCESS, true, false));
            } else {
                logger.info("insert category fail");
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.INSERT_FAIL, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> getListDetail(int idCategory, int page, int size, boolean status, String ip) {
        try {
            Connection conn = ConnectDB.getConnection(urlDB, userDB, passDB);
            CallableStatement callableStatement = conn.prepareCall("{Call category_get_detail(?,?,?,?,?,?,?,?,?,?,?,?)}");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.setInt(3, idCategory);
            callableStatement.setInt(4, 0);
            callableStatement.setInt(5, 0);
            callableStatement.setInt(6, size);
            callableStatement.setInt(7, page);
            callableStatement.setInt(8, 0);
            callableStatement.registerOutParameter(9, Types.INTEGER);
            callableStatement.registerOutParameter(10, Types.INTEGER);
            callableStatement.registerOutParameter(11, Types.INTEGER);
            callableStatement.registerOutParameter(12, Types.INTEGER);
            callableStatement.execute();
            List<JSONObject> listResult = new ArrayList<>();
            ResultSet rsData = null;
            try {
                rsData = callableStatement.getResultSet();
                if (rsData != null) {
                    ResultSetMetaData meta = rsData.getMetaData();
                    int columnCount = meta.getColumnCount();
                    List<String> data = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        data.add(meta.getColumnName(i).toLowerCase(Locale.ROOT));
                    }
                    while (rsData.next()) {
                        JSONObject obj = new JSONObject();
                        for (int i = 1; i < columnCount; i++) {
                            String key = data.get(i - 1);
                            String value = rsData.getString(i);
                            obj.put(key, value);
                        }
                        listResult.add(obj);
                    }
                }
            } catch (Exception e) {
                logger.error("error: " + e.getMessage());
                return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
            } finally {
                if (rsData != null) {
                    rsData.close();
                }
            }
            List<CategoryFieldName> categoryFieldNames = categoryFieldNameRepository.findAllByCategoryId(idCategory);
            PageInfo pageInfo = new PageInfo(1, 10, callableStatement.getInt(9), callableStatement.getInt(10), callableStatement.getInt(11), callableStatement.getInt(12));
            CategoryResponse categoryResponse = new CategoryResponse(pageInfo, listResult, categoryFieldNames);
            logger.info("get list detail category success");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_DETAIL_CATEGORY);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_DETAIL_CATEGORY);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, categoryResponse, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<Resource> exportCSV(int idCategory, String ip) {
        Category category = categoryRepository.getById(Long.valueOf(idCategory));
        if (category != null) {
            String filename = category.getCatTableName() + ".csv";
            logger.info("filename: " + filename);
            InputStreamResource file = new InputStreamResource(fileService.exportCSV(idCategory));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename).contentType(MediaType.parseMediaType("application/csv")).body(file);
        }
        return null;
    }

    @Override
    public void exportPDF(HttpServletResponse response, int idCategory, String ip) {
        try {
            Category category = categoryRepository.getById(Long.valueOf(idCategory));
            if (category != null) {
                String filename = category.getCatTableName();
                logger.info("filename: " + filename);
                response.setContentType("application/pdf");

                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=" + filename + ".pdf";
                response.setHeader(headerKey, headerValue);
                fileService.exportPDF(response, idCategory, category.getCatFullname());
            } else
                return;
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<?> importCSV(MultipartFile file, int idCategory, String ip) {
        return fileService.save(file, idCategory);
    }

    @Override
    public ResponseEntity<?> update(CategoryInsertRequest categoryInsertRequest, int id, String ip) {
        try {
            StringBuilder listValue = new StringBuilder();
            for (LsData dataUpdate : categoryInsertRequest.getLsData()) {
                listValue.append("`").append(dataUpdate.getColumn()).append("`= '").append(dataUpdate.getValue()).append("',");
            }
            listValue.append("`modify_time`").append("= ").append("now()").append(",");
            String listData = listValue.toString();
            listData = listData.substring(0, listData.length() - 1);
            StoredProcedureQuery proc = em.createStoredProcedureQuery("category_update_data");
            proc.registerStoredProcedureParameter("pErrCode", Integer.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pErrText", String.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pCategoryId", Integer.class, ParameterMode.IN);
            proc.registerStoredProcedureParameter("pId", Integer.class, ParameterMode.IN);
            proc.registerStoredProcedureParameter("pListValue", String.class, ParameterMode.IN);
            proc.setParameter("pCategoryId", categoryInsertRequest.getIdCategory());
            proc.setParameter("pId", id);
            proc.setParameter("pListValue", listData);
            proc.execute();
            int code = (int) proc.getOutputParameterValue("pErrCode");
            logger.info("pErrText: " + proc.getOutputParameterValue("pErrText"));
            if (code != 0) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.UPDATE_FAIL, true, false));
            } else {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_CATEGORY);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_READ);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_CATEGORY);
                }
                activityLog.setUsername(userDetails.getUsername());
                activityLog.setIp(ip);
                activityLog.setUserId(user.get().getId());
                activityLogRepository.save(activityLog);
                logger.info("insert success activity log");
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.UPDATE_SUCCESS, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> updateDetail(CategoryInsertRequest categoryInsertRequest, int id, String ip) {
        try {
            StringBuilder listValue = new StringBuilder();
            for (LsData dataUpdate : categoryInsertRequest.getLsData()) {
                listValue.append("`").append(dataUpdate.getColumn()).append("`= '").append(dataUpdate.getValue()).append("',");
            }
            listValue.append("`modify_time`").append("= ").append("now()").append(",");
            String listData = listValue.toString();
            listData = listData.substring(0, listData.length() - 1);
            StoredProcedureQuery proc = em.createStoredProcedureQuery("category_update_data");
            proc.registerStoredProcedureParameter("pErrCode", Integer.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pErrText", String.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pCategoryId", Integer.class, ParameterMode.IN);
            proc.registerStoredProcedureParameter("pId", Integer.class, ParameterMode.IN);
            proc.registerStoredProcedureParameter("pListValue", String.class, ParameterMode.IN);
            proc.setParameter("pCategoryId", categoryInsertRequest.getIdCategory());
            proc.setParameter("pId", id);
            proc.setParameter("pListValue", listData);
            proc.execute();
            int code = (int) proc.getOutputParameterValue("pErrCode");
            logger.info("pErrText: " + proc.getOutputParameterValue("pErrText"));
            if (code != 0) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.UPDATE_FAIL, true, false));
            } else {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_CATEGORY);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_READ);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_CATEGORY);
                }
                activityLog.setUsername(userDetails.getUsername());
                activityLog.setIp(ip);
                activityLog.setUserId(user.get().getId());
                activityLogRepository.save(activityLog);
                logger.info("insert success activity log");
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.UPDATE_SUCCESS, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> delete(int idCategory, String ip) {
        try {
            StoredProcedureQuery proc = em.createStoredProcedureQuery("category_delete");
            proc.registerStoredProcedureParameter("pErrCode", Integer.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pErrText", String.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pCategoryId", Integer.class, ParameterMode.IN);
            proc.setParameter("pCategoryId", idCategory);
            proc.execute();
            int code = (int) proc.getOutputParameterValue("pErrCode");
            if (code == 1) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.DELETE_FAIL, true, false));
            } else {
                if (code == 0) {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_CATEGORY);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_READ);
                        activityLog.setContent(Content.ADMIN + " " + Content.DELETE_CATEGORY);
                    }
                    activityLog.setUsername(userDetails.getUsername());
                    activityLog.setIp(ip);
                    activityLog.setUserId(user.get().getId());
                    activityLogRepository.save(activityLog);
                    logger.info("insert success activity log");
                    return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.DELETE_SUCCESS, true, false));
                } else {
                    return ResponseEntity.ok(new MessageResponse((int) proc.getOutputParameterValue("pErrCode"), proc.getOutputParameterValue("pErrText"), true, false));
                }
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> deleteDetail(int idCategory, int id, String ip) {
        try {
            StoredProcedureQuery proc = em.createStoredProcedureQuery("category_delete_data");
            proc.registerStoredProcedureParameter("pErrCode", Integer.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pErrText", String.class, ParameterMode.OUT);
            proc.registerStoredProcedureParameter("pCategoryId", Integer.class, ParameterMode.IN);
            proc.registerStoredProcedureParameter("pId", Integer.class, ParameterMode.IN);
            proc.setParameter("pCategoryId", idCategory);
            proc.setParameter("pId", id);
            proc.execute();
            int code = (int) proc.getOutputParameterValue("pErrCode");
            if (code == 1) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.DELETE_FAIL, true, false));
            } else {
                if (code == 0) {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_CATEGORY);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_READ);
                        activityLog.setContent(Content.ADMIN + " " + Content.DELETE_CATEGORY);
                    }
                    activityLog.setUsername(userDetails.getUsername());
                    activityLog.setIp(ip);
                    activityLog.setUserId(user.get().getId());
                    activityLogRepository.save(activityLog);
                    logger.info("insert success activity log");
                    return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.DELETE_SUCCESS, true, false));
                } else {
                    return ResponseEntity.ok(new MessageResponse((int) proc.getOutputParameterValue("pErrCode"), proc.getOutputParameterValue("pErrText"), true, false));
                }
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }
}
