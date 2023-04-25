package ru.practicum.main_service.category.exception;

import ru.practicum.main_service.exception.ConflictException;

public class CategoryConflictException extends ConflictException {
    public CategoryConflictException() {
        super("The category is not empty");
    }
}
