package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.RoleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleGroup, Long> {

    Page<RoleGroup> findAllByDeleteFalseAndNameContaining(String name, Pageable pageable);

    Page<RoleGroup> findAllByDeleteFalse(Pageable pageable);

    Optional<RoleGroup> findByName(String name);

    List<RoleGroup> findAllByDeleteFalse();

    boolean existsById(Long id);
}
