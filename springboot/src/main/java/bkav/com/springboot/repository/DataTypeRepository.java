package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.DataType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataTypeRepository extends JpaRepository<DataType, Long> {

    Page<DataType> findAllByNameIsContaining(String name, Pageable pageable);

    Optional<DataType> findByName(String name);
}
