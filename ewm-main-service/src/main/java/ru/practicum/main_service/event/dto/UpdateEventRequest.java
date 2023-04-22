package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.main_service.location.dto.LocationDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;


/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) -
 * значит изменение этих данных не требуется.
 */
@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class UpdateEventRequest<T> {

    /**
     * Новая аннотация
     */
    @Size(min = 20, max = 2000)
    String annotation;

    /**
     * Новая категория
     */
    Long category;

    /**
     * Новое описание
     */
    @Size(min = 20, max = 7000)
    String description;

    /**
     * Новые дата и время на которые намечено событие.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    /**
     * Новая локация {@link LocationDto}
     */
    LocationDto location;


    /**
     * Новое значение флага о платности мероприятия
     */
    Boolean paid;

    /**
     * Новый лимит пользователей
     */
    Integer participantLimit;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    Boolean requestModeration;

    /**
     * Новое состояние события
     */
    T stateAction;

    /**
     * Новый заголовок
     */
    @Size(min = 3, max = 120)
    String title;
}
