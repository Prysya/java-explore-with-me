package ru.practicum.main_service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Данные для добавления новой категории
 */
@Value
@Builder
@AllArgsConstructor
@Jacksonized
public class NewCategoryDto implements Serializable {
    /**
     * Название категории
     */
    @NotBlank
    @Size(min = 1, max = 512)
    String name;
}
