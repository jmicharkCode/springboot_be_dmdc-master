package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.Demo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface DemoRepository extends JpaRepository<Demo, Long> {
    Page<Demo> findAllByDemoNameIsContaining(String name, Pageable pageable);


    @Transactional
    @Procedure(procedureName = "demo_insert_data", outputParameterName = "pErrCode")
    int insertdemo(@Param("DemoId") int idDemo, @Param("PLsColumn") String lsColumn, @Param("pLsData") String lsValue);
}

