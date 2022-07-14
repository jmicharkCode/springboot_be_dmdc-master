package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Modifying
    @Query("delete from UserRole c where c.fkUserRole.userId = :idUser")
    @Transactional
    int delete(@Param("idUser") int idUser);

    @Modifying
    @Query(value = "insert into UserRoleGroup(userId, roleId) VALUES (:userId, :roleId)", nativeQuery = true)
    @Transactional
    void insertUserRole(@Param("userId") int userId, @Param("roleId") int roleId);
}
