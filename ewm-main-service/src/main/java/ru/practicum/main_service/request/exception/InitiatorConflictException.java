package ru.practicum.main_service.request.exception;

import ru.practicum.main_service.exception.ConflictException;

public class InitiatorConflictException extends ConflictException {

    public InitiatorConflictException() {
        super("Event initiator can't make a request for their own event");
    }
}
