package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Permissions")
@Setter
@Getter
public class Permission extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 255)
    @Column(name = "NameRole")
    private String nameRole;

    @NotBlank
    @Size(max = 255)
    @Column(name = "NamePermission")
    private String namePermission;

    @Column(name = "Status")
    private boolean status;

    @Column(name = "IsDelete")
    private boolean delete;
}
