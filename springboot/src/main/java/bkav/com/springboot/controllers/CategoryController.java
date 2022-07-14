package bkav.com.springboot.controllers;

import bkav.com.springboot.payload.request.CategoryInsertRequest;
import bkav.com.springboot.payload.request.CategoryRequest;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(PathResources.CATEGORY)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> getList(@RequestParam(required = false) String name,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "100") int size,
                                     HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.getList(name, (page - 1), size, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST_NEW_CREATE)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> getListNewCreate(@RequestParam(required = false) String name,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "100") int size,
                                              HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.getListNewCreate(ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST_NEW_UPDATE)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> getListNewUpdate(@RequestParam(required = false) String name,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "100") int size,
                                              HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.getListNewUpdate(ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.CREATE)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE')")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequest categoryRequest, HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.create(categoryRequest, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.SAVE)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE')")
    public ResponseEntity<?> insert(@Valid @RequestBody CategoryInsertRequest categoryInsertRequest, HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.insert(categoryInsertRequest, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST_DETAIL)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> getListDetail(@RequestParam(defaultValue = "1") int idCategory,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "100") int size,
                                           @RequestParam(defaultValue = "true") boolean status,
                                           HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.getListDetail(idCategory, page, size, status, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.EXPORT_CSV)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<Resource> exportCSV(@RequestParam(defaultValue = "1") int idCategory,
                                              HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.exportCSV(idCategory, ip);
    }

    @GetMapping(PathResources.EXPORT_PDF)
    public void exportPDF(@RequestParam(defaultValue = "1") int idCategory,
                          HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        categoryService.exportPDF(httpServletResponse, idCategory, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(PathResources.IMPORT_CSV)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> importFileCSV(@RequestParam("file") MultipartFile file,
                                           @RequestParam("idCategory") int idCategory,
                                           HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.importCSV(file, idCategory, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE)
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE') or hasPermission('MANAGER', 'UPDATE')")
    public ResponseEntity<?> update(@RequestParam int id,
                                    @Valid @RequestBody CategoryInsertRequest categoryInsertRequest,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.update(categoryInsertRequest, id, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE_DETAIL)
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE') or hasPermission('MANAGER', 'UPDATE')")
    public ResponseEntity<?> updateDetail(@RequestParam int id,
                                          @Valid @RequestBody CategoryInsertRequest categoryInsertRequest,
                                          HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.updateDetail(categoryInsertRequest, id, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> delete(@RequestParam int idCategory,
                                    HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.delete(idCategory, ip);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(PathResources.DELETE_DETAIL)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public ResponseEntity<?> deleteDetail(@RequestParam int idCategory,
                                          @RequestParam int id,
                                          HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return categoryService.deleteDetail(idCategory, id, ip);
    }
}
