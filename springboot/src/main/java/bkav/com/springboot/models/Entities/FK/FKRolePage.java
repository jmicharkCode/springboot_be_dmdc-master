package bkav.com.springboot.models.Entities.FK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Embeddable
public class FKRolePage implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "RoleId")
    private int roleId;

    @NotBlank
    @Column(name = "PageId")
    private int pageId;

    public FKRolePage() {
    }

    public FKRolePage(int roleId, int pageId) {
        this.roleId = roleId;
        this.pageId = pageId;
    }
}
