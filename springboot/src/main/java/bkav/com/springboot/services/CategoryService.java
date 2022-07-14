package bkav.com.springboot.services;

import bkav.com.springboot.payload.request.CategoryInsertRequest;
import bkav.com.springboot.payload.request.CategoryRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface CategoryService {

    ResponseEntity<?> getList(String name, int page, int size, String ip);

    ResponseEntity<?> getListNewCreate(String ip);

    ResponseEntity<?> getListNewUpdate(String ip);

    ResponseEntity<?> create(CategoryRequest categoryRequest, String ip);

    ResponseEntity<?> insert(CategoryInsertRequest categoryInsertRequest, String ip);

    ResponseEntity<?> getListDetail(int idCategory, int page, int size, boolean status, String ip);

    ResponseEntity<Resource> exportCSV(int idCategory, String ip);

    void exportPDF(HttpServletResponse response, int idCategory, String ip);

    ResponseEntity<?> importCSV(MultipartFile file, int idCategory, String ip);

    ResponseEntity<?> update(CategoryInsertRequest categoryInsertRequest, int id, String ip);

    ResponseEntity<?> updateDetail(CategoryInsertRequest categoryInsertRequest, int id, String ip);

    ResponseEntity<?> delete(int idCategory, String ip);

    ResponseEntity<?> deleteDetail(int idCategory, int id, String ip);
}
