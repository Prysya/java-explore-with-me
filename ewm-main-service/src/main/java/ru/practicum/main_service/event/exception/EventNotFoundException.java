package ru.practicum.main_service.event.exception;

import ru.practicum.main_service.exception.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(Long eventId) {
        super(String.format("Event with id=%d was not found", eventId));
    }
}
