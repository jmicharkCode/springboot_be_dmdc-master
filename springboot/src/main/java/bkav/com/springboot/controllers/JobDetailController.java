package bkav.com.springboot.controllers;

import bkav.com.springboot.ResHelper.ResponseHelper;
import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.JobDetail;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.JobDetailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController
@RequestMapping(PathResources.JOB_DETAIL)
public class JobDetailController {
    @Autowired
    private JobDetailService jobService;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST)
    @PreAuthorize("hasPermission('JOB', 'READ')")
    public Object findAll( HttpServletRequest httpServletRequest) {
        List<JobDetail> jobs = jobService.findAll();
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return ResponseHelper.getResponse(jobs, HttpStatus.OK);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST +  "/{job-id}")
    @PreAuthorize("hasPermission('JOB', 'READ')")
    public Object findById(@PathVariable("job-id") String jobId) {
        JobDetail jobFind = jobService.findById(jobId);
        if(jobFind == null) {
            return ResponseHelper.getErrorResponse("JobId is not existed", HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(jobFind, HttpStatus.OK);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.SAVE )
    @PreAuthorize("hasPermission('JOB', 'CREATE')")
    public Object createNewJob(@RequestBody JobDetail job, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            /*return new ResponseEntity<>(bindingResult.getAllErrors()
                    .stream().map(t-> t.getDefaultMessage()).collect(Collectors.toList())
                    ,HttpStatus.BAD_REQUEST);*/
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        JobDetail newJob = jobService.createNewJob(job);
        return new ResponseEntity<>(newJob, HttpStatus.CREATED);
    }


    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE + "/{job-id}")
    @PreAuthorize("hasPermission('JOB', 'UPDATE')")
    public Object updateJob(@PathVariable("job-id") String jobId
                        ,@RequestBody JobDetail job, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }

       JobDetail updatedJob = jobService.update(Long.parseLong(jobId), job);

        if(updatedJob== null) {
            return ResponseHelper.getErrorResponse("JobId is not existed", HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(updatedJob, HttpStatus.OK);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping (PathResources.DELETE + "/{job-id}")
    @PreAuthorize("hasPermission('JOB', 'DELETE')")
    public Object removeJob(@PathVariable("job-id") String jobId) {
        JobDetail  job = jobService.findById(jobId);
        if(job == null) return ResponseHelper.getResponse("Job Id is not existed", HttpStatus.OK);
        if(jobService.deletedJob(jobId)) {
            return ResponseHelper.getResponse("Success",HttpStatus.OK);
        }
        return ResponseHelper.getResponse("Failed", HttpStatus.OK);
    }

    /*
    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> getList(@RequestParam(required = false) String name,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "100") int size,
                                     HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return jobService.getList(name, (page - 1), size, ip);
    }


    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.SAVE)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE')")
    public ResponseEntity<?> insertJob(@Valid @RequestBody JobDetail job,
                                        HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return jobService.insert(job, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE)
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE')")
    public ResponseEntity<?> updateJob(@Valid @RequestBody JobDetail job, @RequestParam int id,
                                        HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return jobService.update(job, id, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE)
    @PreAuthorize("hasPermission('ADMIN', 'DELETE')")
    public ResponseEntity<?> delete(@RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return jobService.delete(id, ip);
    }
*/

}
