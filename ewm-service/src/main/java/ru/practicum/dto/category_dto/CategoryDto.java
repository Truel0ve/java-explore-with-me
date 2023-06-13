package ru.practicum.dto.category_dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    @Null
    Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}
