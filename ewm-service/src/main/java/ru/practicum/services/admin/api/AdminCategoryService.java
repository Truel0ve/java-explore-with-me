package ru.practicum.services.admin.api;

import ru.practicum.dto.category.CategoryDto;

public interface AdminCategoryService {

    CategoryDto postNewCategory(CategoryDto category);

    void deleteCategory(Long catId);

    CategoryDto patchCategory(Long catId, CategoryDto category);
}