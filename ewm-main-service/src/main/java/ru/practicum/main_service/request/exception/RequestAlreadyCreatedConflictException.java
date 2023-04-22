package ru.practicum.main_service.request.exception;

import ru.practicum.main_service.exception.ConflictException;

public class RequestAlreadyCreatedConflictException extends ConflictException {

    public RequestAlreadyCreatedConflictException() {
        super("Event request already created");
    }
}
