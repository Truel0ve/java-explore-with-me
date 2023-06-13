package ru.practicum.services.admin_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category_dto.CategoryDto;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.models.category.Category;
import ru.practicum.repositories.category.CategoryRepository;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.services.admin_service.api.AdminCategoryService;
import ru.practicum.utility.mapper.CategoryMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto postNewCategory(CategoryDto category) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(category)));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!eventRepository.findByCategoryId(catId).isEmpty()) {
            throw new ValidationException(
                    "The category id=" + catId + " still in use",
                    new IllegalArgumentException());
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(Long catId, CategoryDto category) {
        Optional<Category> optional = categoryRepository.findByName(category.getName());
        if (optional.isPresent() && !optional.get().getId().equals(catId)) {
            throw new ValidationException(
                    "The category name already taken",
                    new IllegalArgumentException());
        }
        categoryRepository.patch(catId, category.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.getCategoryById(catId));
    }
}
