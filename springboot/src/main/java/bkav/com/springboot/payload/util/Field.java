package bkav.com.springboot.payload.util;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Field {

    @NotBlank
    private String nameField;

    @NotBlank
    private String typeField;

    @NotBlank
    private String defaultValue;

    @NotBlank
    private boolean isNull;

    @NotBlank
    private int sizeValue;

    public Field() {
    }

    public Field(String nameField, String typeField, String defaultValue, boolean isNull, int sizeValue) {
        this.nameField = nameField;
        this.typeField = typeField;
        this.defaultValue = defaultValue;
        this.isNull = isNull;
        this.sizeValue = sizeValue;
    }
}
