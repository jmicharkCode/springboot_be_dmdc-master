package bkav.com.springboot.services.impl;

import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.RoleGroup;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.RoleRepository;
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            List<RoleGroup> roles = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<RoleGroup> pageRole;
            if (name == null)
                pageRole = roleRepository.findAllByDeleteFalse(paging);
            else
                pageRole = roleRepository.findAllByDeleteFalseAndNameContaining(name, paging);
            roles = pageRole.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("roles", roles);
            response.put("currentPage", (pageRole.getNumber() + 1));
            response.put("totalItems", roles.size());
            response.put("totalPages", roleRepository.findAllByDeleteFalse().size());
            logger.info("get list role success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_ROLE);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_ROLE);
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
    public ResponseEntity<?> insert(RoleGroup role, String ip) {
        try {
            if (role.getName() == null) {
                return ResponseEntity.ok(new MessageResponse(Status.NULL, Message.INVALID_NULL, true, false));
            } else {
                Optional<RoleGroup> adminRole = roleRepository.findByName(role.getName());
                if (adminRole.isPresent()) {
                    return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
                } else {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal();
                    String username = userDetails.getUsername();
                    role.setCreatedBy(username);
                    role.setUpdatedBy(username);
                    role.setCreateTime(new Date());
                    role.setUpdateTime(new Date());
                    role.setStatus(true);
                    role.setDelete(false);
                    roleRepository.save(role);
                    logger.info("insert success role name: " + role.getName());
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CREATE_ROLE);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_CREATE);
                        activityLog.setContent(Content.ADMIN + " " + Content.CREATE_ROLE);
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
    public ResponseEntity<?> update(RoleGroup roleDto, int id, String ip) {
        try {
            if (!roleRepository.existsById(Long.valueOf(id))) {
                logger.info("role not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                RoleGroup role = roleRepository.getById(Long.valueOf(id));
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                roleDto.setCreatedBy(role.getCreatedBy());
                roleDto.setUpdatedBy(username);
                roleDto.setCreateTime(role.getCreateTime());
                roleDto.setUpdateTime(new Date());
                roleDto.setStatus(true);
                roleDto.setDelete(false);
                BeanUtils.copyProperties(roleDto, role);
                roleRepository.saveAndFlush(role);
                logger.info("update success role name: " + roleDto.getName());
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_ROLE);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_UPDATE);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_ROLE);
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
            RoleGroup role = roleRepository.getById(Long.valueOf(id));
            if (!roleRepository.existsById(Long.valueOf(id))) {
                logger.info("role not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                roleRepository.deleteById(Long.valueOf(id));
                logger.info("delete success role id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_ROLE);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_ROLE);
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
