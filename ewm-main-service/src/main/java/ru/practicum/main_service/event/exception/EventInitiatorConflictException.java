package ru.practicum.main_service.event.exception;

import ru.practicum.main_service.exception.ConflictException;

public class EventInitiatorConflictException extends ConflictException {
    public EventInitiatorConflictException() {
        super("Only the initiator can update an event");
    }
}
