package bkav.com.springboot.controllers;

import bkav.com.springboot.payload.request.ChangePasswordRequest;
import bkav.com.springboot.payload.request.LoginRequest;
import bkav.com.springboot.payload.request.SignupRequest;
import bkav.com.springboot.payload.request.UpdateUserRequest;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(PathResources.AUTH)
public class AuthController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.REGISTER)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.registerAccount(signUpRequest, ip);
    }

    @PostMapping(PathResources.LOGIN)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              @RequestParam(defaultValue = "false") boolean isRemember,
                                              @Param("gRecaptchaResponse") String gRecaptchaResponse,
                                              HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.login(httpServletRequest, loginRequest, isRemember, gRecaptchaResponse, ip);
    }

    @GetMapping(PathResources.LOGOUT)
    public ResponseEntity<?> logout(@Param("token") String token, HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.logout(token, ip);
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
        return accountService.getList(name, (page - 1), size, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE)
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE')")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateUserRequest updateUserRequest,
                                    @RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.update(updateUserRequest, id, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE)
    @PreAuthorize("hasPermission('ADMIN', 'DELETE')")
    public ResponseEntity<?> delete(@RequestParam int id,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.delete(id, ip);
    }

    @PostMapping(PathResources.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                            HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.changePassword(changePasswordRequest, ip);
    }

    @PostMapping(PathResources.RESET_PASSWORD)
    @PreAuthorize("hasPermission('SUPPER', 'RESETPASSWORD')")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                           HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return accountService.resetPassword(changePasswordRequest, ip);
    }

}
