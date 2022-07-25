
package bkav.com.springboot.models.Mapper;

import bkav.com.springboot.models.Dto.DepartmentDto;
import bkav.com.springboot.models.Entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDto toDTO(Department model);
    Department toModel(DepartmentDto dto);
}
