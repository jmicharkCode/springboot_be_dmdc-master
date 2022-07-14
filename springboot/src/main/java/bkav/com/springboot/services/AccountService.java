package bkav.com.springboot.services;

import bkav.com.springboot.payload.request.ChangePasswordRequest;
import bkav.com.springboot.payload.request.LoginRequest;
import bkav.com.springboot.payload.request.SignupRequest;
import bkav.com.springboot.payload.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {

    ResponseEntity<?> registerAccount(SignupRequest signUpRequest, String ip);

    ResponseEntity<?> login(HttpServletRequest request, LoginRequest loginRequest, boolean isRemember, String gRecaptchaResponse, String ip);

    ResponseEntity<?> logout(String token, String ip);

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> update(UpdateUserRequest updateUserRequest, int id, String ip);

    ResponseEntity<?> delete(int id, String ip);

    ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest, String ip);

    ResponseEntity<?> resetPassword(ChangePasswordRequest changePasswordRequest, String ip);
}
