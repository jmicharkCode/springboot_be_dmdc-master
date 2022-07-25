<<<<<<< Updated upstream
package bkav.com.springboot.models.Dto;public class DepartmentDto {
=======
package bkav.com.springboot.models.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDto {

    private Long parentId;

    private String departmentName;

    private boolean isActivated;

    private int order;

    private int level;

    private int hasReceiveWarning;

    private int hasCalendar;

>>>>>>> Stashed changes
}
