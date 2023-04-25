package ru.practicum.main_service.event.exception;

import ru.practicum.main_service.exception.ConflictException;

import java.time.LocalDateTime;

public class EventDateConflictException extends ConflictException {
    public EventDateConflictException(LocalDateTime eventDate) {
        super(String.format(
            "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
            eventDate.toString()
        ));
    }
}
