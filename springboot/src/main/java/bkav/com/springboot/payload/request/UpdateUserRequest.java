package bkav.com.springboot.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class UpdateUserRequest {

    @NotBlank
    private Set<Integer> roles;
}
