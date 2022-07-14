package bkav.com.springboot.services.impl;

import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.Demo;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.DemoRepository;
import bkav.com.springboot.repository.UserRepository;


import bkav.com.springboot.services.DemoService;
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
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoRepository demoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);


    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {

        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Demo> demo = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            org.springframework.data.domain.Page<Demo> pageTest;
            if (name == null) pageTest = demoRepository.findAll(paging);
            else pageTest = demoRepository.findAllByDemoNameIsContaining(name, paging);
            demo = pageTest.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("pages", demo);
            response.put("currentPage", (pageTest.getNumber() + 1));
            response.put("totalItems", demo.size());
            response.put("totalPages", demoRepository.findAll().size());
            logger.info("get list demo success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_DEMO);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_DEMO);
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
    public ResponseEntity<?> insert(Demo demo, String ip) {
        try {
            demoRepository.save(demo);
            logger.info("insert demo success");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.INSERT_DEMO);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.INSERT_DEMO);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.INSERT_SUCCESS, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }


    @Override
    public ResponseEntity<?> update(Demo demo, int id, String ip) {
        try {
            Demo demo1 = demoRepository.getById(Long.valueOf(id));
            if (demo1 == null) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.UPDATE_FAIL, true, false));
            } else {
                Demo demo2 = new Demo();
                demo2.setId(demo1.getId());
                demo2.setDemoName(demo.getDemoName());
                demo2.setDemoType(demo.isDemoType());
                demo2.setCreateTime(demo1.getCreateTime());
                demo2.setStatus(demo.isStatus());
                BeanUtils.copyProperties(demo2, demo1);
                demoRepository.saveAndFlush(demo1);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_DEMO);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_READ);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_DEMO);
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
    public ResponseEntity<?> delete(int id, String ip) {
        try {

            if (!demoRepository.existsById(Long.valueOf(id))) {
                logger.info("demo not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                demoRepository.deleteById(Long.valueOf(id));

                logger.info("delete success demo id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_DEMO);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_DEMO);
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
