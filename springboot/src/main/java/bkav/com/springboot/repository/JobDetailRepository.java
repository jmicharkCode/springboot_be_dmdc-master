package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.JobDetail;
<<<<<<< Updated upstream
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {
=======
import bkav.com.springboot.models.Entities.RoleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {
    Optional<JobDetail> findById(String id);
>>>>>>> Stashed changes
}
