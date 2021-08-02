package pro.xway.file_storage.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.xway.file_storage.dao.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String name);

    boolean existsByEmail(String email);
}

