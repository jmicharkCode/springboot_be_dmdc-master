package bkav.com.springboot.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private boolean rememberPassword;
    private int id;

    public JwtResponse(String token, String username, String email, boolean rememberPassword, int id) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.rememberPassword = rememberPassword;
        this.id = id;
    }
}
