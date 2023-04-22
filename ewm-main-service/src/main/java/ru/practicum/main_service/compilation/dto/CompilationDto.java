package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main_service.event.dto.EventShortDto;

import java.io.Serializable;
import java.util.List;

/**
 * Подборка событий
 */
@Value
@Builder
@AllArgsConstructor
public class CompilationDto implements Serializable {
    /**
     * Идентификатор
     */
    Long id;

    /**
     * Список {@link EventShortDto}
     */
    transient List<EventShortDto> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    Boolean pinned;

    /**
     * Заголовок подборки
     */
    String title;
}
