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
@Table(name = "CategoryFieldName")
@Getter
@Setter
public class CategoryFieldName extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "CategoryId")
    private int categoryId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "FieldName")
    private String fieldName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "FieldFullName")
    private String fieldFullName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "DataType")
    private String dataType;

    @Column(name = "DefaultValue")
    private String defaultValue;

    @Column(name = "NotNull")
    private boolean notNull;
}
