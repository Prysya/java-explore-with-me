package ru.practicum.main_service.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main_service.request.constant.RequestStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Заявка на участие в событии
 */
@Value
@Builder
@AllArgsConstructor
public class ParticipationRequestDto implements Serializable {
    /**
     * Идентификатор заявки
     */
    Long id;

    /**
     * Дата и время создания заявки
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;

    /**
     * Идентификатор события
     */
    Long event;

    /**
     * Идентификатор пользователя, отправившего заявку
     */
    Long requester;


    /**
     * Статус заявки
     */
    RequestStatus status;
}
