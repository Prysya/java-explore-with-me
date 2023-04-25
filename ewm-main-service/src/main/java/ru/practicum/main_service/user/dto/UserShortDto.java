package ru.practicum.main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * Пользователь (короткая информация)
 */
@Value
@Builder
@AllArgsConstructor
public class UserShortDto implements Serializable {

    /**
     * Идентификатор
     */
    Long id;

    /**
     * Имя
     */
    String name;
}
