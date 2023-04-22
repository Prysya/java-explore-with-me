package ru.practicum.main_service.request.exception;

import ru.practicum.main_service.exception.ConflictException;

public class RequestUserConflictException extends ConflictException {

    public RequestUserConflictException() {
        super("You can't cancel another user's request");
    }
}
