package bkav.com.springboot.models.Entities.FK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Embeddable
public class FKUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "UserId")
    private int userId;

    @NotBlank
    @Column(name = "RoleId")
    private int roleId;

    public FKUserRole() {
    }

    public FKUserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
