package bkav.com.springboot.payload.response;

import bkav.com.springboot.models.Entities.CategoryFieldName;
import org.json.simple.JSONObject;

import java.util.List;

public class CategoryResponse {

    private PageInfo page_info;

    private List<JSONObject> data;

    private List<CategoryFieldName> fields;

    public CategoryResponse(PageInfo page_info, List<JSONObject> data, List<CategoryFieldName> fields) {
        this.page_info = page_info;
        this.data = data;
        this.fields = fields;
    }

    public CategoryResponse() {
    }

    public PageInfo getPage_info() {
        return page_info;
    }

    public void setPage_info(PageInfo page_info) {
        this.page_info = page_info;
    }

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }

    public List<CategoryFieldName> getFields() {
        return fields;
    }

    public void setFields(List<CategoryFieldName> fields) {
        this.fields = fields;
    }
}
