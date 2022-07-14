package bkav.com.springboot.models.Mapper;

import bkav.com.springboot.models.Entities.Category;
import bkav.com.springboot.models.Entities.CategoryFieldName;

import java.util.Date;

public class MapCategory {

    public static Category getCategory(String nameCategory, String fullNameCategory, boolean categoryType, String username) {
        Category category = new Category();
        category.setCatTableName(nameCategory);
        category.setCatFullname(fullNameCategory);
        category.setCatType(categoryType);
        category.setStatus(true);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        category.setCreatedBy(username);
        category.setUpdatedBy(username);
        return category;
    }

    public static CategoryFieldName getCategoryFieldName(Long idCategory, String fieldName, String fieldFullName, String dataType, String defaultValue, boolean isNull, String username) {
        CategoryFieldName categoryFieldName = new CategoryFieldName();
        categoryFieldName.setCategoryId(Math.toIntExact(idCategory));
        categoryFieldName.setFieldName(fieldName);
        categoryFieldName.setFieldFullName(fieldFullName);
        categoryFieldName.setDataType(dataType);
        categoryFieldName.setDefaultValue(defaultValue);
        categoryFieldName.setNotNull(isNull);
        categoryFieldName.setCreateTime(new Date());
        categoryFieldName.setUpdateTime(new Date());
        categoryFieldName.setCreatedBy(username);
        categoryFieldName.setUpdatedBy(username);
        return categoryFieldName;
    }
}
