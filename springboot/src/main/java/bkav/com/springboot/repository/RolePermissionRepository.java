package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    @Transactional
    @Modifying
    @Query("delete from RolePermission c where c.fkPermissionRole.roleId = :idRole")
    int delete(@Param("idRole") int idRole);
}
