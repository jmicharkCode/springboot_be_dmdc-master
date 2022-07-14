package bkav.com.springboot.services;

import bkav.com.springboot.models.Entities.RoleGroup;
import org.springframework.http.ResponseEntity;

public interface RoleService {

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> insert(RoleGroup role, String ip);

    ResponseEntity<?> update(RoleGroup role, int id, String ip);

    ResponseEntity<?> delete(int id, String ip);
}
