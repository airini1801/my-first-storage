package pro.xway.file_storage.services;


import org.springframework.stereotype.Service;
import pro.xway.file_storage.dao.entities.User;
import pro.xway.file_storage.dto.CategoryDto;
import pro.xway.file_storage.dao.entities.Category;
import pro.xway.file_storage.dao.repositories.CategoryRepository;
import pro.xway.file_storage.exception.ApplicationException;
import pro.xway.file_storage.exception.ExceptionEnum;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public void createDefaultCategory(User user) {
        long count = categoryRepository.count();
        if (count == 0) {
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(new Category("Файлы", user));
            categoryList.add(new Category("Фото", user));
            categoryList.add(new Category("Альбомы", user));
            categoryList.add(new Category("Корзина", user));
            categoryRepository.saveAll(categoryList);
        }
    }


    public List<Category> getCategoriesByUser() {
        return categoryRepository.findAllByUser(userService.getCurrentUser());
    }


    public void createAndSave(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setUser(userService.getCurrentUser());
        categoryRepository.save(category);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public Category getCategoryById(Long categoryId) throws ApplicationException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();

        } else {
            System.out.println("Category not found by id - " + categoryId);
            throw new ApplicationException(ExceptionEnum.CATEGORY_ID_NOT_EXIST);
        }
    }
}
