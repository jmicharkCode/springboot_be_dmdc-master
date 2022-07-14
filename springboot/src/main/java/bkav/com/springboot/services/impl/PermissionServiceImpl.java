package bkav.com.springboot.services.impl;

import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.Permission;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.PermissionRepository;
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            List<Permission> permissions = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Permission> pagePermission;
            if (name == null)
                pagePermission = permissionRepository.findAllByDeleteFalse(paging);
            else
                pagePermission = permissionRepository.findAllByDeleteFalseAndNameRoleContaining(name, paging);
            permissions = pagePermission.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("permissions", permissions);
            response.put("currentPage", (pagePermission.getNumber() + 1));
            response.put("totalItems", permissions.size());
            response.put("totalPages", permissionRepository.findAllByDeleteFalse().size());
            logger.info("get list permission success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_PERMISSION);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_PERMISSION);
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
    public ResponseEntity<?> insert(Permission permission, String ip) {
        try {
            if (permission.getNameRole() == null) {
                return ResponseEntity.ok(new MessageResponse(Status.NULL, Message.INVALID_NULL, true, false));
            } else {
                Optional<Permission> per = permissionRepository.findByNameRole(permission.getNameRole());
                if (per.isPresent()) {
                    return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
                } else {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal();
                    String username = userDetails.getUsername();
                    permission.setCreatedBy(username);
                    permission.setUpdatedBy(username);
                    permission.setCreateTime(new Date());
                    permission.setUpdateTime(new Date());
                    permission.setDelete(false);
                    permission.setStatus(true);
                    permissionRepository.save(permission);
                    logger.info("insert success permission name: " + permission.getNameRole());
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CREATE_PERMISSION);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_CREATE);
                        activityLog.setContent(Content.ADMIN + " " + Content.CREATE_PERMISSION);
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
    public ResponseEntity<?> update(Permission permission, int id, String ip) {
        try {
            if (!permissionRepository.existsById(Long.valueOf(id))) {
                logger.info("permission not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.NOT_EXITS, Message.NOT_EXITS, true, false));
            } else {
                Permission per = permissionRepository.getById(Long.valueOf(id));
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                permission.setCreatedBy(per.getCreatedBy());
                permission.setUpdatedBy(username);
                permission.setCreateTime(per.getCreateTime());
                permission.setUpdateTime(new Date());
                permission.setStatus(true);
                permission.setDelete(false);
                BeanUtils.copyProperties(permission, per);
                permissionRepository.saveAndFlush(per);
                logger.info("update success permission name: " + per.getNameRole());
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_PERMISSION);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_UPDATE);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_PERMISSION);
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
            return ResponseEntity.badRequest().body((new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true)));
        }
    }

    @Override
    public ResponseEntity<?> delete(int id, String ip) {
        try {

            if (!permissionRepository.existsById(Long.valueOf(id))) {
                logger.info("permission not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.NOT_EXITS, Message.NOT_EXITS, true, false));
            } else {
                Permission per = permissionRepository.getById(Long.valueOf(id));
                permissionRepository.delete(per);
                logger.info("delete success permission id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_PERMISSION);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_PERMISSION);
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
