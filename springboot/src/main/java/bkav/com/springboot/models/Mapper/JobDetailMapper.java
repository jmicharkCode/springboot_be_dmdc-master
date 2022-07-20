
package bkav.com.springboot.models.Mapper;

import bkav.com.springboot.models.Dto.JobDetailDto;
import bkav.com.springboot.models.Entities.JobDetail;
import org.mapstruct.factory.Mappers;

public class JobDetailMapper {
    public JobDetailDto toDto(JobDetail model) {
        if ( model == null ) {
            return null;
        }

        JobDetailDto jobDetailDto = new JobDetailDto();

        jobDetailDto.setId( model.getId() );
        jobDetailDto.setName( model.getName() );

        return jobDetailDto;
    }
    public JobDetail toModel(JobDetailDto dto) {
        if ( dto == null ) {
            return null;
        }

        JobDetail jobDetail = new JobDetail();

        jobDetail.setId( dto.getId() );
        jobDetail.setName( dto.getName() );

        return jobDetail;
    }

}
