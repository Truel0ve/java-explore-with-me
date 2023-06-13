package ru.practicum.repositories.category;

import org.springframework.context.annotation.Lazy;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.category.Category;
@SuppressWarnings("all")
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{
    private final CategoryRepository categoryRepository;

    public CategoryRepositoryImpl(@Lazy CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified category id=" + catId + " does not exist",
                        new NullPointerException()));
    }
}
