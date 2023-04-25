package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Value
@Builder
@AllArgsConstructor
public class NewCompilationDto implements Serializable {
    /**
     * Список идентификаторов событий входящих в подборку
     */
    @NotNull
    transient Set<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    @NotNull
    @Builder.Default
    Boolean pinned = false;

    /**
     * Заголовок подборки
     */
    @NotBlank
    @Size(min = 1, max = 512)
    String title;
}
