package ru.practicum.main_service.user.exception;

import ru.practicum.main_service.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id=%d was not found", userId));
    }
}
