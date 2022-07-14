package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "DataType", uniqueConstraints = {
        @UniqueConstraint(columnNames = "Name")
})
@Getter
@Setter
public class DataType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 255)
    @Column(name = "Name")
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(name = "Code")
    private String code;

    @NotBlank
    @Size(max = 255)
    @Column(name = "Length")
    private int length;
}
