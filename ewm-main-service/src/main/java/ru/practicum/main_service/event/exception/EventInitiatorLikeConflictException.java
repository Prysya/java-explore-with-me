package ru.practicum.main_service.event.exception;

import ru.practicum.main_service.exception.ConflictException;

public class EventInitiatorLikeConflictException extends ConflictException {
    public EventInitiatorLikeConflictException() {
        super("The initiator cannot change the rating of his event");
    }
}
