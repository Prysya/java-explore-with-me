package ru.practicum.main_service.request.exception;

import ru.practicum.main_service.exception.ConflictException;

public class ParticipantLimitConflictException extends ConflictException {

    public ParticipantLimitConflictException() {
        super("The participant limit has been reached");
    }
}
