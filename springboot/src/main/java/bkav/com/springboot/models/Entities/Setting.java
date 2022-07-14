package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "Setting")
@Getter
@Setter
public class Setting extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "Name")
    private String name;

    @NotBlank
    @Column(name = "Status")
    private boolean status;

    @Column(name = "IsDelete")
    private boolean delete;

    @Column(name = "GiaTri")
    private int giaTri;
}
