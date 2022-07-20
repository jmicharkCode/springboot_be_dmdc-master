package bkav.com.springboot.services;

import bkav.com.springboot.models.Entities.Demo;
import bkav.com.springboot.models.Entities.Position;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PositionService {
    List<Position> getList();
    ResponseEntity<?> insert(Position position);
    ResponseEntity<?> update(Position position, int id);
    ResponseEntity<?> delete(int id);
}
