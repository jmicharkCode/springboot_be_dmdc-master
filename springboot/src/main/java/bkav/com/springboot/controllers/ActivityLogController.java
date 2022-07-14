package bkav.com.springboot.controllers;

import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.ActivityLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(PathResources.ACTIVITY_LOG)
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST)
    @PreAuthorize("hasPermission('ADMIN', 'READ')")
    public ResponseEntity<?> getList(@RequestParam(required = false) String name,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "100") int size,
                                     @RequestParam(value = "start_time", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_time,
                                     @RequestParam(value = "end_time", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_time,
                                     HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return activityLogService.getList(name, (page - 1), size, start_time, end_time, ip);
    }
}
