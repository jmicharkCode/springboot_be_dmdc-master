package bkav.com.springboot.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface ActivityLogService {

    ResponseEntity<?> getList(String name, int page, int size, Date startTime, Date endTime, String ip);
}
