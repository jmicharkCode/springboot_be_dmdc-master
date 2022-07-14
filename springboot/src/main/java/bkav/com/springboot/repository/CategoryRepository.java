package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAllByCatTableNameIsContaining(String name, Pageable pageable);

    Optional<Category> findByCatTableName(String name);

    List<Category> findTop5ByOrderByCreateTimeDesc();

    List<Category> findTop5ByOrderByUpdateTimeDesc();

    @Transactional
    @Procedure(procedureName = "create_category")
    void createCategory(@Param("pSqlQuery") String sqlQuery);

    @Transactional
    @Procedure(procedureName = "category_insert_data", outputParameterName = "pErrCode")
    int insertCategory(@Param("pCategoryId") int idCategory, @Param("PLsColumn") String lsColumn, @Param("pLsData") String lsValue);
}
