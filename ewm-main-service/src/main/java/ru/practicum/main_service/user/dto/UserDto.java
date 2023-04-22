package ru.practicum.main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * Пользователь
 */
@Value
@Builder
@AllArgsConstructor
public class UserDto implements Serializable {

    /**
     * Идентификатор
     */
    Long id;

    /**
     * Почтовый адрес
     */
    String email;

    /**
     * Имя
     */
    String name;
}
