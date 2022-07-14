package bkav.com.springboot.services.impl;

import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.Page;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.PageRepository;
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.PageService;
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
public class PageServiceImpl implements PageService {

    private static final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            List<Page> pages = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            org.springframework.data.domain.Page<Page> pagePage;
            if (name == null)
                pagePage = pageRepository.findAllByDeleteFalse(paging);
            else
                pagePage = pageRepository.findAllByDeleteFalseAndNameContaining(name, paging);
            pages = pagePage.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("pages", pages);
            response.put("currentPage", (pagePage.getNumber() + 1));
            response.put("totalItems", pageRepository.findAllByDeleteFalse().size());
            response.put("totalPages", pagePage.getTotalPages());
            logger.info("get list page success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_PAGE);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_PAGE);
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
    public ResponseEntity<?> insert(Page page, String ip) {
//        try {
            if (page.getName() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.NULL, Message.INVALID_NULL, true, false));
            } else {
                Optional<Page> per = pageRepository.findByName(page.getName());
                if (per.isPresent()) {
                    return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
                } else {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal();
                    String username = userDetails.getUsername();
                    page.setCreatedBy(username);
                    page.setUpdatedBy(username);
                    page.setCreateTime(new Date());
                    page.setUpdateTime(new Date());
                    page.setDelete(false);
                    page.setStatus(true);
                    pageRepository.save(page);
                    logger.info("insert success page name: " + page.getName());
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CREATE_PAGE);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_CREATE);
                        activityLog.setContent(Content.ADMIN + " " + Content.CREATE_PAGE);
                    }
                    activityLog.setUsername(userDetails.getUsername());
                    activityLog.setIp(ip);
                    activityLog.setUserId(user.get().getId());
                    activityLogRepository.save(activityLog);
                    logger.info("insert success activity log");
                    return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.INSERT_SUCCESS, true, false));
                }
            }
//        } catch (Exception e) {
//            logger.error("error: " + e.getMessage());
//            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
//        }
    }

    @Override
    public ResponseEntity<?> update(Page page, int id, String ip) {
        try {
            if (!pageRepository.existsById(Long.valueOf(id))) {
                logger.info("page not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                Page pg = pageRepository.getById(Long.valueOf(id));
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                page.setCreatedBy(pg.getCreatedBy());
                page.setUpdatedBy(username);
                page.setCreateTime(pg.getCreateTime());
                page.setUpdateTime(new Date());
                page.setStatus(true);
                page.setDelete(false);
                BeanUtils.copyProperties(page, pg);
                pageRepository.saveAndFlush(pg);
                logger.info("update success page name: " + page.getName());
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_PAGE);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_UPDATE);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_PAGE);
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
            if (!pageRepository.existsById(Long.valueOf(id))) {
                logger.info("page not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                Optional<Page> per = pageRepository.findById(Long.valueOf(id));
                pageRepository.delete(per.get());
                logger.info("delete success page id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_PAGE);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_PAGE);
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
