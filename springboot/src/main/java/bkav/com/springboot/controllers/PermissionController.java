package bkav.com.springboot.controllers;

import bkav.com.springboot.models.Entities.Permission;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.PermissionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(PathResources.PERMISSION)
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

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
        return permissionService.getList(name, (page - 1), size, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.SAVE)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE')")
    public ResponseEntity<?> insert(@Valid @RequestBody Permission permission,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return permissionService.insert(permission, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE)
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE')")
    public ResponseEntity<?> update(@Valid @RequestBody Permission permission,
                                    @RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return permissionService.update(permission, id, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE)
    @PreAuthorize("hasPermission('ADMIN', 'DELETE')")
    public ResponseEntity<?> delete(@RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return permissionService.delete(id, ip);
    }
}
