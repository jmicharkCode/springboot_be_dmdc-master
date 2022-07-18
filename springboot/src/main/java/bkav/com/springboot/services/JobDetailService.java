package bkav.com.springboot.services;

// import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.JobDetail;
import bkav.com.springboot.models.Entities.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface JobDetailService {

    List<JobDetail> findAll();

    JobDetail createNewJob(JobDetail job);

    JobDetail update(String Id, JobDetail job);

    JobDetail removeJob(String jobId);

    JobDetail findById(Long jobId);
}