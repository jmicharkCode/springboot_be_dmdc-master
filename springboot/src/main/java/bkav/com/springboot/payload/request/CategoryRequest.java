package bkav.com.springboot.payload.request;

import bkav.com.springboot.payload.util.Field;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank
    private String nameCategory;

    @NotBlank
    private boolean typeCategory;

    @NotBlank
    private Set<Field> fields;

    public CategoryRequest() {
    }

    public CategoryRequest(String nameCategory, boolean typeCategory, Set<Field> fields) {
        this.nameCategory = nameCategory;
        this.typeCategory = typeCategory;
        this.fields = fields;
    }
}
