<<<<<<< Updated upstream
package bkav.com.springboot.controllers;public class DepartmentController {
=======
package bkav.com.springboot.controllers;

import bkav.com.springboot.ResHelper.ResponseHelper;
import bkav.com.springboot.models.Dto.DepartmentDto;
import bkav.com.springboot.models.Entities.Department;
import bkav.com.springboot.payload.util.PathResources;
import bkav.com.springboot.repository.DepartmentRepository;
import bkav.com.springboot.services.DepartmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathResources.DEPARTMENT)
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @Autowired
    private DepartmentRepository repository;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST)
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public Object findAll( HttpServletRequest httpServletRequest) {
        List<Department> deps = service.findAll();
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        ip = httpServletRequest.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return ResponseHelper.getResponse(deps, HttpStatus.OK);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(PathResources.GET_LIST + "/{department-id}")
    @PreAuthorize("hasPermission('ADMIN', 'READ') or hasPermission('MANAGER', 'READ')")
    public Object findById(@PathVariable("department-id") String id) {
        Department dep = service.findById(id);
        if(dep == null) {
            return ResponseHelper.getResponse("Department ID is not existed", HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(dep, HttpStatus.OK);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping (PathResources.CREATE)
    @PreAuthorize("hasPermission('ADMIN', 'CREATE') OR hasPermission('MANAGER', 'CREATE')")
    public Object createNewDepartment(@Valid @RequestBody Department dep
                                    , BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        Department newDep = service.createNewDepartment(dep);
        return ResponseHelper.getResponse(newDep, HttpStatus.CREATED);
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping(PathResources.UPDATE + "/{department-id}")
    @PreAuthorize("hasPermission('ADMIN', 'UPDATE') or hasPermission('MANAGER', 'UPDATE')")
    public Object updateDepartment(@PathVariable("department-id") String id,
                                            @RequestBody Department dep, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }

        Department updatedDep = service.update(id, dep);

        if(updatedDep == null) {
            return ResponseHelper.getErrorResponse("Department Id can't not found", HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(updatedDep, HttpStatus.OK);
    }


    @ApiOperation(value = "", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping (PathResources.DELETE + "/{department-id}")
    @PreAuthorize("hasPermission('ADMIN', 'DELETE') or hasPermission('MANAGER', 'DELETE')")
    public Object removeJob(@PathVariable("department-id") String id) {
        Department dep = service.findById(id);
        if(dep == null) return ResponseHelper.getResponse("Job Id is not existed", HttpStatus.OK);
        if(service.delete(id)) {
            return ResponseHelper.getResponse("Success",HttpStatus.OK);
        }
        return ResponseHelper.getResponse("Failed", HttpStatus.OK);
    }
>>>>>>> Stashed changes
}
