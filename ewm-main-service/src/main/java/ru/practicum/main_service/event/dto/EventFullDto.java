package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.location.dto.LocationDto;
import ru.practicum.main_service.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class EventFullDto {
    /**
     * Идентификатор
     */
    Long id;

    /**
     * Краткое описание
     */
    String annotation;

    /**
     * Категория {@link CategoryDto}
     */
    CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    Integer confirmedRequests;

    /**
     * Дата и время создания события
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    /**
     * Полное описание события
     */
    String description;

    /**
     * Дата и время на которые намечено событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    /**
     * Инициатор события {@link UserShortDto}
     */
    UserShortDto initiator;

    /**
     * Локация проведения события {@link LocationDto}
     */
    LocationDto location;

    /**
     * Нужно ли оплачивать участие
     */
    Boolean paid;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    Integer participantLimit;

    /**
     * Дата и время публикации события
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    Boolean requestModeration;

    /**
     * Заголовок
     */
    String title;

    /**
     * Жизненный цикл события
     */
    EventState state;

    /**
     * Количество просмотрев события
     */
    Integer views;
}
