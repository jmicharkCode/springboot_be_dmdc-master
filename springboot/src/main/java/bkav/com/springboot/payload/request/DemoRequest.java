package bkav.com.springboot.payload.request;

import bkav.com.springboot.payload.util.Field;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class DemoRequest {

    @NotBlank
    private String nameDemo;

    private boolean typeDemo;

    @NotBlank
    private Set<Field> fields;


}
