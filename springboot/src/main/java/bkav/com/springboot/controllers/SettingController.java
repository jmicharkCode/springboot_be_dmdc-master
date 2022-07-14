package bkav.com.springboot.controllers;

import bkav.com.springboot.models.Dto.SettingLogDebugDto;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.SettingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(PathResources.SETTING)
public class SettingController {

    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.LOG_DEBUG)
    @PreAuthorize("hasPermission('SUPPER', 'SETTING')")
    public ResponseEntity<?> getList(@RequestBody SettingLogDebugDto settingLogDebugDto,
                                     HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return settingService.updateLogDebug(settingLogDebugDto.isStatus(), settingLogDebugDto.getId(), ip);
    }

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
        return settingService.getList(name, (page - 1), size, ip);
    }
}
