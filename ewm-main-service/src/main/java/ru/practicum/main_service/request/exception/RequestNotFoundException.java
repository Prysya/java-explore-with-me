package ru.practicum.main_service.request.exception;

import ru.practicum.main_service.exception.NotFoundException;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(Long participationId) {
        super(String.format("Request with id=%d was not found", participationId));
    }
}
