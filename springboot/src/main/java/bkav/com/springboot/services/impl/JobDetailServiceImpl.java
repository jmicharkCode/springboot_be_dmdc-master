package bkav.com.springboot.services.impl;


import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.ActivityLog;
import bkav.com.springboot.models.Entities.JobDetail;

import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.models.Mapper.JobDetailMapper;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.ActivityLogRepository;
import bkav.com.springboot.repository.JobDetailRepository;
import bkav.com.springboot.repository.UserRepository;
import bkav.com.springboot.services.JobDetailService;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobDetailServiceImpl implements JobDetailService {
    @Autowired
    private JobDetailRepository jobRepository;

    public List<JobDetail> findAll() {
        List<JobDetail> jobs = jobRepository.findAll();
        return jobs;
    }

   public JobDetail createNewJob(JobDetail job) {
        JobDetail newJob = jobRepository.save(job);
        return newJob;

   }


    @Override
    public JobDetail removeJob(String jobId) {
        return null;
    }

    @Override
    public JobDetail findById(Long jobId) {
        Optional<JobDetail> jobOpt = jobRepository.findById(jobId);
        return jobOpt.orElse(null);
    }

    public JobDetail update(String id, JobDetail job) {
       Optional<JobDetail> jobOpt = jobRepository.findById(id);

       if(!jobOpt.isPresent()) {
           return null;
       }

       JobDetail currentJob = jobOpt.get();
       if(!currentJob.getId().equals(job.getId())) {

           Optional<JobDetail> existedJob = jobRepository.findById(job.getId());

           if(existedJob.isPresent()) {
               return null;
           }

          currentJob.setId(job.getId());
       }

       currentJob.setName(job.getName());
       return jobRepository.save(currentJob) ;
   }

   /* @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            List<JobDetail> jobs = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<JobDetail> pageJob;
            if (name == null)
                pageJob = jobRepository.findAllByDeleteFalse(paging);
            else
                pageJob = jobRepository.findAllByDeleteFalseAndNameContaining(name, paging);
            jobs = pageJob.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("jobs", jobs);
            response.put("currentPage", (pageJob.getNumber() + 1));
            response.put("totalItems", jobs.size());
            response.put("totalPages", jobRepository.findAllByDeleteFalse().size());
            logger.info("get list role success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_JOB);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_JOB);
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
    public ResponseEntity<?> insert(JobDetail job, String ip) {
        try {
            if (job.getName() == null) {
                return ResponseEntity.ok(new MessageResponse(Status.NULL, Message.INVALID_NULL, true, false));
            } else {
                Optional<JobDetail> adminJob = jobRepository.findByName(job.getName());
                if (adminJob.isPresent()) {
                    return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
                } else {
                    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal();
                    String username = userDetails.getUsername();
                    job.setCreatedBy(username);
                    job.setUpdatedBy(username);
                    job.setCreateTime(new Date());
                    job.setUpdateTime(new Date());
                    jobRepository.save(job);
                    logger.info("insert success job name: " + job.getName());
                    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                    ActivityLog activityLog = new ActivityLog();
                    if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                        activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                        activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CREATE_JOB);
                    } else {
                        activityLog.setActivityType(ActivityType.ADMIN_CREATE);
                        activityLog.setContent(Content.ADMIN + " " + Content.CREATE_JOB);
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
    public ResponseEntity<?> update(JobDetail jobDetail, int id, String ip) {
        try {
            if (!jobRepository.existsById(Long.valueOf(id))) {
                logger.info("role not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                JobDetail job = jobRepository.getById(Long.valueOf(id));
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                jobDetail.setCreatedBy(job.getCreatedBy());
                jobDetail.setUpdatedBy(username);
                jobDetail.setCreateTime(job.getCreateTime());
                jobDetail.setUpdateTime(new Date());
                BeanUtils.copyProperties(jobDetail, job);

                jobRepository.saveAndFlush(job);
                logger.info("update success job name: " + job.getName());
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_JOB);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_UPDATE);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_JOB);
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
            JobDetail job = jobRepository.getById(Long.valueOf(id));
            if (!jobRepository.existsById(Long.valueOf(id))) {
                logger.info("role not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                jobRepository.deleteById(Long.valueOf(id));
                logger.info("delete success role id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_JOB);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_JOB);
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
    }*/
}
