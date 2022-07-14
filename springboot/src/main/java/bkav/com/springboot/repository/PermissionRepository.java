package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Page<Permission> findAllByDeleteFalseAndNameRoleContaining(String name, Pageable pageable);

    Page<Permission> findAllByDeleteFalse(Pageable pageable);

    Optional<Permission> findByNameRole(String name);

    List<Permission> findAllByDeleteFalse();

    boolean existsById(Long id);
}
