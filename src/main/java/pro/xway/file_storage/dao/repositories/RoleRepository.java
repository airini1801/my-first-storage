package pro.xway.file_storage.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.xway.file_storage.dao.entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
