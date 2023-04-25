package ru.practicum.main_service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * Категория
 */
@Value
@Builder
@AllArgsConstructor
public class CategoryDto implements Serializable {
    /**
     * Идентификатор категории
     */
    Long id;
    /**
     * Название категории
     */
    String name;
}
