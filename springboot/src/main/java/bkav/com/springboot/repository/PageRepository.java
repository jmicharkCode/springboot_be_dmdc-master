package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    org.springframework.data.domain.Page<Page> findAllByDeleteFalseAndNameContaining(String name, Pageable pageable);

    org.springframework.data.domain.Page<Page> findAllByDeleteFalse(Pageable pageable);

    Optional<Page> findByName(String name);

    List<Page> findAllByDeleteFalse();

    boolean existsById(Long id);
}
