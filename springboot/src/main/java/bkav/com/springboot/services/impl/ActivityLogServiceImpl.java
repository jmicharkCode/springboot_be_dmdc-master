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
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.ActivityLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, Date startTime, Date endTime, String ip) {
        try {
            if (startTime == null || endTime == null) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.TIME_NOT_NULL, true, false));
            }
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            List<ActivityLog> activityLogs = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            org.springframework.data.domain.Page<ActivityLog> pageActivityLog;
            if (name == null)
                pageActivityLog = activityLogRepository.findAllByCreateTimeBetween(startTime, endTime, paging);
            else
                pageActivityLog = activityLogRepository.findAllByCreateTimeBetweenAndContentContaining(startTime, endTime, name, paging);
            activityLogs = pageActivityLog.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("pages", activityLogs);
            response.put("currentPage", (pageActivityLog.getNumber() + 1));
            response.put("totalItems", pageActivityLog.getTotalElements() + 1);
            response.put("totalPages", pageActivityLog.getTotalPages());
            logger.info("get list activity log success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_ACTIVITY_LOG);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_ACTIVITY_LOG);
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
}
