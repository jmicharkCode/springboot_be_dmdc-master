package bkav.com.springboot.services.impl;

import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.DataType;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.DataTypeRepository;
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.DataTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataTypeImpl implements DataTypeService {

    private static final Logger logger = LoggerFactory.getLogger(DataTypeImpl.class);

    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            List<DataType> dataTypes = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            org.springframework.data.domain.Page<DataType> pageDataType;
            if (name == null)
                pageDataType = dataTypeRepository.findAll(paging);
            else
                pageDataType = dataTypeRepository.findAllByNameIsContaining(name, paging);
            dataTypes = pageDataType.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("pages", dataTypes);
            response.put("currentPage", (pageDataType.getNumber() + 1));
            response.put("totalItems", dataTypeRepository.findAll().size());
            response.put("totalPages", pageDataType.getTotalPages());
            logger.info("get list data type success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_DATA_TYPE);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_DATA_TYPE);
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
    public ResponseEntity<?> insert(DataType dataType, String ip) {
        try {
            if (dataType.getName() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.NULL, Message.INVALID_NULL, true, false));
            } else {
                Optional<DataType> dataType1 = dataTypeRepository.findByName(dataType.getName());
                if (dataType1.isPresent()) {
                    return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
                } else {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal();
                    String username = userDetails.getUsername();
                    dataType.setCreatedBy(username);
                    dataType.setUpdatedBy(username);
                    dataType.setCreateTime(new Date());
                    dataType.setUpdateTime(new Date());
                    dataTypeRepository.save(dataType);
                    logger.info("insert success data type name: " + dataType.getName());
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CREATE_DATA_TYPE);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_CREATE);
                        activityLog.setContent(Content.ADMIN + " " + Content.CREATE_DATA_TYPE);
                    }
                    activityLog.setUsername(userDetails.getUsername());
                    activityLog.setIp(ip);
                    activityLog.setUserId(user.get().getId());
                    activityLogRepository.save(activityLog);
                    logger.info("insert success activity log");
                    return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.INSERT_SUCCESS, true, false));
                }
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> update(DataType dataType, int id, String ip) {
        try {
            if (!dataTypeRepository.existsById(Long.valueOf(id))) {
                logger.info("page not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                DataType dataType1 = dataTypeRepository.getById(Long.valueOf(id));
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                dataType.setCreatedBy(dataType1.getCreatedBy());
                dataType.setUpdatedBy(username);
                dataType.setCreateTime(dataType1.getCreateTime());
                dataType.setUpdateTime(new Date());
                BeanUtils.copyProperties(dataType, dataType1);
                dataTypeRepository.saveAndFlush(dataType1);
                logger.info("update success data type name: " + dataType.getName());
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_DATA_TYPE);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_UPDATE);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_DATA_TYPE);
                }
                activityLog.setUsername(userDetails.getUsername());
                activityLog.setIp(ip);
                activityLog.setUserId(user.get().getId());
                activityLogRepository.save(activityLog);
                logger.info("update success activity log");
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.UPDATE_SUCCESS, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> delete(int id, String ip) {
        try {
            if (!dataTypeRepository.existsById(Long.valueOf(id))) {
                logger.info("page not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                Optional<DataType> dataType = dataTypeRepository.findById(Long.valueOf(id));
                dataTypeRepository.delete(dataType.get());
                logger.info("delete success data type id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_DATA_TYPE);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_DATA_TYPE);
                }
                activityLog.setUsername(userDetails.getUsername());
                activityLog.setIp(ip);
                activityLog.setUserId(user.get().getId());
                activityLogRepository.save(activityLog);
                logger.info("delete success activity log");
                return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.DELETE_SUCCESS, true, false));
            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }
}
