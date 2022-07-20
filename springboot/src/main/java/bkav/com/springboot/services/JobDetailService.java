package bkav.com.springboot.services;
import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.JobDetail;


import java.util.List;

public interface JobDetailService {

    List<JobDetail> findAll();
    JobDetail findById(String jobId);

    JobDetail createNewJob(JobDetail job);

    JobDetail update(Long Id, JobDetail job);

    boolean deletedJob(String jobId);

}