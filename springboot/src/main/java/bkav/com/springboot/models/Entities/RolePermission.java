package bkav.com.springboot.models.Entities;

import bkav.com.springboot.models.Entities.FK.FKRolePermission;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RoleGroupPermission")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FKRolePermission fkPermissionRole;

    public RolePermission(FKRolePermission fkPermissionRole) {
        this.fkPermissionRole = fkPermissionRole;
    }

    public RolePermission() {

    }
}
