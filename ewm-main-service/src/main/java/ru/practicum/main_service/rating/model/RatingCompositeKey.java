package ru.practicum.main_service.rating.model;

import java.io.Serializable;

public class RatingCompositeKey implements Serializable {
    private Long userId;
    private Long eventId;
}
