package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.RolePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePageRepository extends JpaRepository<RolePage, Long> {
}
