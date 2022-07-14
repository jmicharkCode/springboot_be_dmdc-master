package bkav.com.springboot.services.impl;

import bkav.com.springboot.captcha.VerifyCaptcha;
import bkav.com.springboot.models.Entities.*;
import bkav.com.springboot.payload.request.ChangePasswordRequest;
import bkav.com.springboot.payload.request.LoginRequest;
import bkav.com.springboot.payload.request.SignupRequest;
import bkav.com.springboot.payload.request.UpdateUserRequest;
import bkav.com.springboot.payload.response.JwtResponse;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.ActivityType;
import bkav.com.springboot.payload.util.Content;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.*;
import bkav.com.springboot.security.jwt.JwtUtils;
import bkav.com.springboot.security.services.UserDetailsImpl;
import bkav.com.springboot.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private RedisTemplate template;

    @Autowired
    private SettingRepository settingRepository;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public ResponseEntity<?> registerAccount(SignupRequest signUpRequest, String ip) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.USERNAME_EXITS, true, false));
            }
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.EMAIL_EXITS, true, false));
            }
            if (!isValid(signUpRequest.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_FORMAT, true, false));
            }
            if (containsSequences(signUpRequest.getUsername(), signUpRequest.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_CONTAINS, true, false));
            }
            User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
            Set<Integer> strRoles = signUpRequest.getRoles();
            Set<RoleGroup> roles = new HashSet<>();

            if (strRoles == null) {
                Optional<RoleGroup> roleGroup = roleRepository.findByName("permission-role");
                if (roleGroup.isPresent()) {
                    return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.REGISTER_FAIL, true, false));
                }
                roles.add(roleGroup.get());
            } else {
                strRoles.stream().forEach(id -> {
                    Optional<RoleGroup> roleGroup = roleRepository.findById(Long.valueOf(id));
                    roles.add(roleGroup.get());
                });
            }
            user.setRoles(roles);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            user.setCreatedBy(username);
            user.setUpdatedBy(username);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setStatus(true);
            user.setDelete(false);
            userRepository.save(user);
            Optional<User> user1 = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.REGISTER);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.REGISTER);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user1.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");

            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.REGISTER_SUCCESS, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.ok(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> login(HttpServletRequest request, LoginRequest loginRequest, boolean isRemember, String gRecaptchaResponse, String ip) {
        try {
            if (gRecaptchaResponse != null) {
                if (VerifyCaptcha.verify(gRecaptchaResponse) == false)
                    return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.CHECK_CAPTCHA, true, false));
            }
            Optional<User> userRequest = userRepository.findByUsername(loginRequest.getUsername());
            if (!userRequest.isPresent()) {
                if (countRequest(request))
                    return ResponseEntity.ok(new MessageResponse(Status.CAPTCHA, Message.LOGIN_CAPTCHA, true, false));
                else return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.LOGIN_FAIL, true, false));
            }
            boolean result = encoder.matches(loginRequest.getPassword(), userRequest.get().getPassword());
            if (result == false) {
                if (countRequest(request))
                    return ResponseEntity.ok(new MessageResponse(Status.CAPTCHA, Message.LOGIN_CAPTCHA, true, false));
                else return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.LOGIN_FAIL, true, false));
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            int expiration_Token = 0;
            if (isRemember) {
                expiration_Token = 345600000;
            } else {
                Setting setting = settingRepository.getById(Long.valueOf(2));
                if (setting != null) {
                    expiration_Token = setting.getGiaTri();
                }
            }
            String jwt = jwtUtils.generateJwtToken(authentication, expiration_Token);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            // save redis
            template.opsForValue().set(userDetails.getUsername(), userDetails, 60, TimeUnit.MINUTES);
            template.opsForValue().set(jwt, jwt, 60, TimeUnit.MINUTES);
            logger.info("login success username: " + userDetails.getUsername());
            boolean status = false;
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if (user.get().getRememberPassword() == 1) status = true;
            Optional<User> user1 = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.LOGIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.LOGIN);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_CREATE);
                activityLog.setContent(Content.ADMIN + " " + Content.LOGIN);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user1.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), status, Math.toIntExact(user.get().getId())), true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.ok(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> logout(String token, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> user1 = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            activityLog.setActivityType(ActivityType.LOGOUT);
            activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.LOGOUT);
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user1.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            template.delete(token);
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.LOGOUT_SUCCESS, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.ok(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> getList(String name, int page, int size, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<User> users = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            org.springframework.data.domain.Page<User> pageUser;
            if (name == null) pageUser = userRepository.findAllByDeleteFalse(paging);
            else pageUser = userRepository.findAllByDeleteFalseAndUsernameContaining(name, paging);
            users = pageUser.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("pages", users);
            response.put("currentPage", (pageUser.getNumber() + 1));
            response.put("totalItems", userRepository.findAllByDeleteFalse().size());
            response.put("totalPages", pageUser.getTotalPages());
            logger.info("get list page success page: " + page + " size: " + size);
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.GET_LIST_USER);
            } else {
                activityLog.setActivityType(ActivityType.ADMIN_READ);
                activityLog.setContent(Content.ADMIN + " " + Content.GET_LIST_USER);
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
    public ResponseEntity<?> update(UpdateUserRequest user, int id, String ip) {
        try {
            if (!userRepository.existsById(Long.valueOf(id))) {
                logger.info("user not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username = userDetails.getUsername();
                Set<Integer> strRoles = user.getRoles();
                if (strRoles == null) {
                    Optional<RoleGroup> roleGroup = roleRepository.findByName("ROLE_USER");
                    if (roleGroup.isPresent()) {
                        return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.REGISTER_FAIL, true, false));
                    }
                    int idRole = Math.toIntExact(roleGroup.get().getId());
                    userRoleRepository.insertUserRole(id, idRole);
                } else {
                    Set<RoleGroup> roleGroups = new HashSet<>();
                    strRoles.stream().forEach(idRole -> {
                        Optional<RoleGroup> roleGroup = roleRepository.findById(Long.valueOf(idRole));
                        roleGroups.add(roleGroup.get());
                    });
                    User user1 = userRepository.getById(Long.valueOf(id));
                    User user2 = new User();
                    user2.setId(user1.getId());
                    user2.setUsername(user1.getUsername());
                    user2.setPassword(user1.getPassword());
                    user2.setEmail(user1.getEmail());
                    user2.setCreatedBy(user1.getCreatedBy());
                    user2.setUpdatedBy(username);
                    user2.setCreateTime(user1.getCreateTime());
                    user2.setUpdateTime(new Date());
                    user2.setRoles(roleGroups);
                    user2.setStatus(true);
                    user2.setDelete(false);
                    BeanUtils.copyProperties(user2, user1);
                    userRepository.saveAndFlush(user1);
                }
                logger.info("update success user id: " + id);
                Optional<User> user1 = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.UPDATE_USER);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_UPDATE);
                    activityLog.setContent(Content.ADMIN + " " + Content.UPDATE_USER);
                }
                activityLog.setUsername(userDetails.getUsername());
                activityLog.setIp(ip);
                activityLog.setUserId(user1.get().getId());
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
            if (!userRepository.existsById(Long.valueOf(id))) {
                logger.info("user not exits id: " + id);
                return ResponseEntity.ok(new MessageResponse(Status.EXITS, Message.EXITS, true, false));
            } else {
                userRoleRepository.delete(id);
                userRepository.deleteById(Long.valueOf(id));
                logger.info("delete success page id: " + id);
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
                ActivityLog activityLog = new ActivityLog();
                if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                    activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                    activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.DELETE_USER);
                } else {
                    activityLog.setActivityType(ActivityType.ADMIN_DELETE);
                    activityLog.setContent(Content.ADMIN + " " + Content.DELETE_USER);
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
    }

    @Override
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            User userDto = userRepository.getById(Long.valueOf(changePasswordRequest.getId()));
            if (userDto == null) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.USER_NOT_EXITS, true, false));
            }
            if (!isValid(changePasswordRequest.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_FORMAT, true, false));
            }
            if (containsSequences(userDto.getUsername(), changePasswordRequest.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_CONTAINS, true, false));
            }
            boolean result = encoder.matches(changePasswordRequest.getPassword(), userDto.getPassword());
            if (result) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_DUPLICATE, true, false));
            }
            User user2 = new User();
            user2.setId(userDto.getId());
            user2.setUsername(userDto.getUsername());
            user2.setPassword(encoder.encode(changePasswordRequest.getPassword()));
            user2.setEmail(userDto.getEmail());
            user2.setCreatedBy(userDto.getCreatedBy());
            user2.setUpdatedBy(user.get().getUsername());
            user2.setCreateTime(userDto.getCreateTime());
            user2.setUpdateTime(new Date());
            user2.setRoles(userDto.getRoles());
            user2.setStatus(true);
            user2.setDelete(false);
            BeanUtils.copyProperties(user2, userDto);
            userRepository.saveAndFlush(userDto);
            logger.info("change password success user id: " + changePasswordRequest.getId());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CHANGE_PASSWORD);
            } else {
                activityLog.setActivityType(ActivityType.CHANGE_PASSWORD);
                activityLog.setContent(Content.ADMIN + " " + Content.CHANGE_PASSWORD);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.CHANGE_PASSWORD, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(ChangePasswordRequest changePasswordRequest, String ip) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            User userDto = userRepository.getById(Long.valueOf(changePasswordRequest.getId()));
            if (userDto == null) {
                return ResponseEntity.ok(new MessageResponse(Status.FAIL, Message.USER_NOT_EXITS, true, false));
            }
            if (!isValid(changePasswordRequest.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_FORMAT, true, false));
            }
            if (containsSequences(userDto.getUsername(), changePasswordRequest.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, Message.PASSWORD_CONTAINS, true, false));
            }
            User user2 = new User();
            user2.setId(userDto.getId());
            user2.setUsername(userDto.getUsername());
            user2.setPassword(encoder.encode(changePasswordRequest.getPassword()));
            user2.setEmail(userDto.getEmail());
            user2.setCreatedBy(userDto.getCreatedBy());
            user2.setUpdatedBy(user.get().getUsername());
            user2.setCreateTime(userDto.getCreateTime());
            user2.setUpdateTime(new Date());
            user2.setRoles(userDto.getRoles());
            user2.setStatus(true);
            user2.setDelete(false);
            BeanUtils.copyProperties(user2, userDto);
            userRepository.saveAndFlush(userDto);
            logger.info("change password success user id: " + changePasswordRequest.getId());
            ActivityLog activityLog = new ActivityLog();
            if (userDetails.getUsername().equalsIgnoreCase("supper_admin")) {
                activityLog.setActivityType(ActivityType.SUPPER_ADMIN);
                activityLog.setContent(Content.SUPPER_ADMIN + " " + Content.CHANGE_PASSWORD);
            } else {
                activityLog.setActivityType(ActivityType.CHANGE_PASSWORD);
                activityLog.setContent(Content.ADMIN + " " + Content.CHANGE_PASSWORD);
            }
            activityLog.setUsername(userDetails.getUsername());
            activityLog.setIp(ip);
            activityLog.setUserId(user.get().getId());
            activityLogRepository.save(activityLog);
            logger.info("insert success activity log");
            return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, Message.CHANGE_PASSWORD, true, false));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(Status.EXCEPTION, Message.EXCEPTION, false, true));
        }
    }

    public boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Boolean containsSequences(String uname, String pwd) {
        Boolean contains = false;
        int count = 0;
        for (int i = 0; i < pwd.length(); i++) {
            String kyTu = String.valueOf(pwd.charAt(i));
            if (uname.contains(kyTu)) {
                count++;
            }
        }
        if (count > 4) {
            contains = true;
        }
        return contains;
    }

    public boolean countRequest(HttpServletRequest request) {
        try {
            int count = 1;
            Setting setting_N = settingRepository.getById(Long.valueOf(3));
            int n_token = setting_N.getGiaTri();
            Setting setting_X = settingRepository.getById(Long.valueOf(4));
            int x_token = setting_X.getGiaTri();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            logger.info("ipAddress: " + ipAddress);
            HttpSession session = request.getSession();
            logger.info("session: " + session.getId());
            if (template.opsForValue().get(ipAddress) != null) {
                int countOld = (int) template.opsForValue().get(ipAddress) + 1;
                template.opsForValue().set(ipAddress, countOld, template.getExpire(ipAddress, TimeUnit.SECONDS), TimeUnit.SECONDS);
            } else {
                template.opsForValue().set(ipAddress, count, (x_token * 60), TimeUnit.SECONDS);
            }
            if (template.opsForValue().get(session.getId()) != null) {
                int countOld = (int) template.opsForValue().get(session.getId()) + 1;
                template.opsForValue().set(session.getId(), countOld, template.getExpire(session.getId(), TimeUnit.SECONDS), TimeUnit.SECONDS);
            } else {
                template.opsForValue().set(session.getId(), count, (x_token * 60), TimeUnit.SECONDS);
            }
            logger.info("count ipAddress: " + template.opsForValue().get(ipAddress));
            logger.info("ipAddress time expire: " + template.getExpire(ipAddress, TimeUnit.SECONDS));
            logger.info("count session id: " + template.opsForValue().get(session.getId()));
            logger.info("session id time expire: " + template.getExpire(session.getId(), TimeUnit.SECONDS));
            if ((int) template.opsForValue().get(ipAddress) > n_token || (int) template.opsForValue().get(session.getId()) > n_token)
                return true;
            else return false;
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return false;
        }
    }
}
