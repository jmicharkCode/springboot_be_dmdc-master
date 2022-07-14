package bkav.com.springboot.services;

import bkav.com.springboot.models.Entities.Demo;
import org.springframework.http.ResponseEntity;


public interface DemoService {


    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> insert(Demo demo, String ip);


    ResponseEntity<?> update(Demo demo, int id, String ip);

    ResponseEntity<?> delete(int id, String ip);


}


