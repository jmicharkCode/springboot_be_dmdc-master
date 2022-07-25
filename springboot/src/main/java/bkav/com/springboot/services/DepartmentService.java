
package bkav.com.springboot.services;

import bkav.com.springboot.models.Dto.DepartmentDto;
import bkav.com.springboot.models.Entities.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    Department findById(String id);
    Department update(String id, Department dep);
   // DepartmentDto createNewDepartment(DepartmentDto dto);

    Department createNewDepartment(Department dto);
    boolean delete(String id) ;
}
