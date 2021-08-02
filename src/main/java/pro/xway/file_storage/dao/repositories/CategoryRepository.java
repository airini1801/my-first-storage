package pro.xway.file_storage.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.xway.file_storage.dao.entities.Category;
import pro.xway.file_storage.dao.entities.User;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

        void deleteAllByName(String name);
        List<Category> findAllByUser(User user);
}
