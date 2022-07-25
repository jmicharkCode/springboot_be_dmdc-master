
package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Department d SET d.departmentIdExt = :idExt WHERE d.id = :depId")
    void saveDepartmentIdExt(@Param("depId") Long companyId, @Param("idExt") String address);
}
