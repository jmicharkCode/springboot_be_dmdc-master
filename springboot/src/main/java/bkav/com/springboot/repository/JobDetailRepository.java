package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {
}
