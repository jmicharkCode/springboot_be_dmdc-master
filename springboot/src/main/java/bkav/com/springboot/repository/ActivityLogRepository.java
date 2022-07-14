package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    Page<ActivityLog> findAllByCreateTimeBetweenAndContentContaining(Date startTime, Date endTime, String content, Pageable pageable);

    Page<ActivityLog> findAllByCreateTimeBetween(Date startTime, Date endTime, Pageable pageable);
}
