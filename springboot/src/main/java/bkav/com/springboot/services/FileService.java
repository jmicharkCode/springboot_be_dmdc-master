package bkav.com.springboot.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

public interface FileService {

    static String storeFile(MultipartFile file) {
        return null;
    }

    void exportPDF(HttpServletResponse response, int idCategory, String filename);

    ByteArrayInputStream exportCSV(int idCategory);

    ResponseEntity<?> save(MultipartFile file, int idCategory);

    static Resource loadFileAsResource(String fileName) {
        return null;
    }
}
