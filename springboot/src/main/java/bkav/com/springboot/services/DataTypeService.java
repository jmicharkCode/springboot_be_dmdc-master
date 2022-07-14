package bkav.com.springboot.services;

import bkav.com.springboot.models.Entities.DataType;
import org.springframework.http.ResponseEntity;

public interface DataTypeService {

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> insert(DataType dataType, String ip);

    ResponseEntity<?> update(DataType dataType, int id, String ip);

    ResponseEntity<?> delete(int id, String ip);
}
