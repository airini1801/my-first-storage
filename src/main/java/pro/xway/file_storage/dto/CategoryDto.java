package pro.xway.file_storage.dto;

import pro.xway.file_storage.dao.entities.Category;

public class CategoryDto {
    private Long id;
    private String name;

    public CategoryDto() {
    }

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
