package bkav.com.springboot.payload.request;

public class ChangePasswordRequest {
    private int id;

    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ChangePasswordRequest(int id, String password) {
        this.id = id;
        this.password = password;
    }

}
