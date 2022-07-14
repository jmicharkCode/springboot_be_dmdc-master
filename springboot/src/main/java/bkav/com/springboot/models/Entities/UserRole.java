package bkav.com.springboot.models.Entities;

import bkav.com.springboot.models.Entities.FK.FKUserRole;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UserRoleGroup")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FKUserRole fkUserRole;

    public UserRole(FKUserRole fkUserRole) {
        this.fkUserRole = fkUserRole;
    }

    public UserRole() {

    }
}
