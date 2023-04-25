package ru.practicum.main_service.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * Широта и долгота места проведения события
 */
@Value
@Builder
@AllArgsConstructor
public class LocationDto implements Serializable {
    /**
     * Широта
     */
    Float lat;

    /**
     * Долгота
     */
    Float lon;
}
