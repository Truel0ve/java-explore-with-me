package ru.practicum.services.public_service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category_dto.CategoryDto;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.repositories.category.CategoryRepository;
import ru.practicum.services.public_service.api.PublicCategoryService;
import ru.practicum.utility.PageableBuilder;
import ru.practicum.utility.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {
    CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageableBuilder.getPageable(from, size))
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified category id=" + catId + " does not exist",
                        new NullPointerException()))); // 404
    }
}
