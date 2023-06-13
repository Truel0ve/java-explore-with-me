package ru.practicum.services.admin_service.api;

import ru.practicum.dto.category_dto.CategoryDto;

public interface AdminCategoryService {
    CategoryDto postNewCategory(CategoryDto category);
    void deleteCategory(Long catId);
    CategoryDto patchCategory(Long catId, CategoryDto category);
}
