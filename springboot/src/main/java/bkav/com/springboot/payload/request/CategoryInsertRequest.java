package bkav.com.springboot.payload.request;

import bkav.com.springboot.payload.util.LsData;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CategoryInsertRequest {

    @NotBlank
    private int idCategory;

    @NotBlank
    private List<LsData> lsData;

    public CategoryInsertRequest(int idCategory, List<LsData> lsData) {
        this.idCategory = idCategory;
        this.lsData = lsData;
    }

    public CategoryInsertRequest() {
    }
}
