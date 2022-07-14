package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.Setting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    Page<Setting> findAllByDeleteFalseAndNameContaining(String name, Pageable pageable);

    Page<Setting> findAllByDeleteFalse(Pageable pageable);

    List<Setting> findAllByDeleteFalse();

    Setting findByName(String name);

    @Transactional
    @Modifying
    @Query("update Setting s set s.status = :status where s.id = :id")
    void updateLogDebug(@Param("status") boolean status, @Param("id") Long id);
}
