package bkav.com.springboot.payload.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LsData {

    private String column;

    private String value;

    public LsData() {
    }

    public LsData(String column, String value) {
        this.column = column;
        this.value = value;
    }
}
