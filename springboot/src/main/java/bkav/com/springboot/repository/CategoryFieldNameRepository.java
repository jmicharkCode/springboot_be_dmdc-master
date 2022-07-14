package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.CategoryFieldName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryFieldNameRepository extends JpaRepository<CategoryFieldName, Long> {

    List<CategoryFieldName> findAllByCategoryId(int idCategory);

    CategoryFieldName findCategoryFieldNameByCategoryIdAndFieldName(int idCategory, String fieldName);
}
