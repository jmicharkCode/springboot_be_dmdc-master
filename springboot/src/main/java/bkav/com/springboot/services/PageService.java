package bkav.com.springboot.services;

import bkav.com.springboot.models.Entities.Page;
import org.springframework.http.ResponseEntity;

public interface PageService {

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> insert(Page page, String ip);

    ResponseEntity<?> update(Page page, int id, String ip);

    ResponseEntity<?> delete(int id, String ip);
}
