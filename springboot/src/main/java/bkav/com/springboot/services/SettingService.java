package bkav.com.springboot.services;

import org.springframework.http.ResponseEntity;

public interface SettingService {

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> updateLogDebug(boolean status, int id, String ip);
}
