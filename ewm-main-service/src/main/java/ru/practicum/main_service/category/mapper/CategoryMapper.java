package ru.practicum.main_service.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;
import ru.practicum.main_service.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public static Category toEntity(NewCategoryDto newCategoryDto) {
        return Category.builder()
            .name(newCategoryDto.getName())
            .build();
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }

}
