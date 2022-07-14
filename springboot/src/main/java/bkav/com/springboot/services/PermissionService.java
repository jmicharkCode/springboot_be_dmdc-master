package bkav.com.springboot.services;

import bkav.com.springboot.models.Entities.Permission;
import org.springframework.http.ResponseEntity;

public interface PermissionService {

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> insert(Permission permission, String ip);

    ResponseEntity<?> update(Permission permission, int id, String ip);

    ResponseEntity<?> delete(int id, String ip);
 }
