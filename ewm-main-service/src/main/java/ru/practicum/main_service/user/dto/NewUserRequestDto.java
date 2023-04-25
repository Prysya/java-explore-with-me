package ru.practicum.main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Данные нового пользователя
 */
@Value
@Builder
@AllArgsConstructor
public class NewUserRequestDto implements Serializable {
    /**
     * Почтовый адрес
     */
    @NotNull
    @Email
    @Size(min = 1, max = 256)
    String email;

    /**
     * Имя
     */
    @NotBlank
    @Size(min = 1, max = 256)
    String name;
}
