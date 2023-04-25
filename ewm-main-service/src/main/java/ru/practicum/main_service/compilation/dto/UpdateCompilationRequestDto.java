package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main_service.event.dto.EventFullDto;

import java.io.Serializable;
import java.util.List;

/**
 * Изменение информации о подборке событий. Если поле в запросе не указано (равно null) -
 * значит изменение этих данных не требуется.
 */
@Value
@Builder
@AllArgsConstructor
public class UpdateCompilationRequestDto implements Serializable {

    /**
     * Список идентификаторов ${@link EventFullDto}
     */
    transient List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    Boolean pinned;

    /**
     * Заголовок подборки
     */
    String title;
}
