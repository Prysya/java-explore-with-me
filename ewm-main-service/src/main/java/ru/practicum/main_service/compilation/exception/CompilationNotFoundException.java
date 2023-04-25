package ru.practicum.main_service.compilation.exception;

import ru.practicum.main_service.exception.NotFoundException;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(Long compilationId) {
        super(String.format("Compilation with id=%d was not found", compilationId));
    }
}
