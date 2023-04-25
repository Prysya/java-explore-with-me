package ru.practicum.main_service.request.exception;

import ru.practicum.main_service.exception.ConflictException;

public class UnpublishedEventConflictException extends ConflictException {

    public UnpublishedEventConflictException() {
        super("You can't participate in an unpublished event");
    }
}
