package ru.practicum.repositories.category;

import ru.practicum.models.category.Category;

public interface CategoryRepositoryCustom {
    Category getCategoryById(Long catId);
}
