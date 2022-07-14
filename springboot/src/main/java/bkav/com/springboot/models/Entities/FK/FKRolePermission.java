package bkav.com.springboot.models.Entities.FK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Embeddable
public class FKRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "RoleId")
    private int roleId;

    @NotBlank
    @Column(name = "PermissionId")
    private int permissionId;

    public FKRolePermission() {
    }

    public FKRolePermission(int roleId, int permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
