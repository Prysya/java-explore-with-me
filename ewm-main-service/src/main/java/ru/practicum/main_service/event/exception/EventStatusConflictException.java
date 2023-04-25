package ru.practicum.main_service.event.exception;

import ru.practicum.main_service.exception.ConflictException;

public class EventStatusConflictException extends ConflictException {
    public EventStatusConflictException() {
        super("Only pending or canceled events can be changed");
    }
}
