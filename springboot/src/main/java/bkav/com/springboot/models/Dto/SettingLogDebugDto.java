package bkav.com.springboot.models.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SettingLogDebugDto {

    @NotBlank
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    @NotBlank
    private int id;

    public int getId() {
        return id;
    }
}
