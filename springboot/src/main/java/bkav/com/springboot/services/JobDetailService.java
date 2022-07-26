package bkav.com.springboot.services;
<<<<<<< Updated upstream
import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.JobDetail;

=======

// import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.JobDetail;
import bkav.com.springboot.models.Entities.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
>>>>>>> Stashed changes

import java.util.List;

public interface JobDetailService {

    List<JobDetail> findAll();
<<<<<<< Updated upstream
    JobDetail findById(String jobId);

    JobDetail createNewJob(JobDetail job);

    JobDetail update(Long Id, JobDetail job);

    boolean deletedJob(String jobId);

=======

    JobDetail createNewJob(JobDetail job);

    JobDetail update(String Id, JobDetail job);

    JobDetail removeJob(String jobId);

    JobDetail findById(Long jobId);
>>>>>>> Stashed changes
}