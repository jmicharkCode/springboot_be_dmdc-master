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
@Table(name = "Category", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CatTableName")
})
@Getter
@Setter
public class Category extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 255)
    @Column(name = "CatTableName")
    private String catTableName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "CatFullName")
    private String catFullname;

    @NotBlank
    @Column(name = "CatType")
    private boolean catType;

    @NotBlank
    @Column(name = "Status")
    private boolean status;
}
