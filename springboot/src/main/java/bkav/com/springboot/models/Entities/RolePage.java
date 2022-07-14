package bkav.com.springboot.models.Entities;

import bkav.com.springboot.models.Entities.FK.FKRolePage;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RoleGroupPage")
public class RolePage implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FKRolePage fkRolePage;

    public RolePage(FKRolePage fkRolePage) {
        this.fkRolePage = fkRolePage;
    }

    public RolePage() {

    }
}
