package bkav.com.springboot.controllers;

import bkav.com.springboot.payload.util.PathResources;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping(PathResources.FILE)
public class FileController {

    @GetMapping(PathResources.EXCEL_CREATE_CATEGORY)
    public ResponseEntity<?> getFileExcelCreateCategory(HttpServletRequest request) throws IOException {
        String path = "./fileexcel/excel-template.xlsx";
        File file = new File(path);
        String filename = "excel-template.xlsx";
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

}
