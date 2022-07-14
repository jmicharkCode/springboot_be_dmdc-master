package bkav.com.springboot.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResponse {

    private int Status;
    private CategoryResponse Object;
    private boolean isOk;
    private boolean isError;

    public TestResponse(int status, CategoryResponse object, boolean isOk, boolean isError) {
        Status = status;
        Object = object;
        this.isOk = isOk;
        this.isError = isError;
    }
}
