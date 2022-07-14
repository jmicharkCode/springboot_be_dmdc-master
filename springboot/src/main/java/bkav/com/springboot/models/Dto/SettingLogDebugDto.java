package bkav.com.springboot.models.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class SettingLogDebugDto {

    @NotBlank
    private boolean status;

    @NotBlank
    private int id;

    public SettingLogDebugDto(boolean status, int id) {
        this.status = status;
        this.id = id;
    }

    public SettingLogDebugDto() {
    }
}
