package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.location.dto.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Новое событие
 */
@Value
@Builder
@AllArgsConstructor
@Jacksonized
public class NewEventDto {

    /**
     * Краткое описание
     */
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    /**
     * Категория {@link CategoryDto}
     */
    @NotNull
    @Positive
    Long category;

    /**
     * Полное описание события
     */
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;

    /**
     * Дата и время на которые намечено событие
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    /**
     * Локация проведения события {@link LocationDto}
     */
    @NotNull
    LocationDto location;


    /**
     * Нужно ли оплачивать участие
     */
    @Builder.Default
    Boolean paid = false;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    @Builder.Default
    @PositiveOrZero
    Integer participantLimit = 0;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    @Builder.Default
    Boolean requestModeration = true;

    /**
     * Заголовок
     */
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
