package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.user.dto.UserShortDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class EventShortDto implements Serializable {
    /**
     * Идентификатор
     */
    Long id;
    /**
     * Краткое описание
     */
    String annotation;

    /**
     * {@link CategoryDto}
     */
    transient CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    Integer confirmedRequests;

    /**
     * Дата и время на которые намечено событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    /**
     * Инициатор события {@link UserShortDto}
     */
    transient UserShortDto initiator;

    /**
     * Нужно ли оплачивать участие
     */
    Boolean paid;

    /**
     * Заголовок
     */
    String title;

    /**
     * Количество просмотрев события
     */
    Integer views;
}
