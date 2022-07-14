package bkav.com.springboot.controllers;

import bkav.com.springboot.models.Entities.Demo;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.DemoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(PathResources.DEMO)

public class DemoController {

    @Autowired
    private DemoService demoService;

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
        return demoService.getList(name, (page - 1), size, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.SAVE)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE')")
    public ResponseEntity<?> insert(@RequestBody Demo demo, HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return demoService.insert(demo, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE)
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE') or hasPermission('MANAGER', 'UPDATE')")
    public ResponseEntity<?> update(@RequestParam int id,
                                    @RequestBody Demo demo,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return demoService.update(demo, id, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> delete(@RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return demoService.delete(id, ip);
    }
}



