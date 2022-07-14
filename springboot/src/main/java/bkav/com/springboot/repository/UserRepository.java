package bkav.com.springboot.repository;

import bkav.com.springboot.models.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    boolean existsById(Long id);

    Page<User> findAllByDeleteFalseAndUsernameContaining(String name, Pageable pageable);

    Page<User> findAllByDeleteFalse(Pageable pageable);

    List<User> findAllByDeleteFalse();
}
