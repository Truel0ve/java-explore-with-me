package ru.practicum.exceptions;

public class ArgumentNotFoundException extends RuntimeException {
    public ArgumentNotFoundException(final String message) {
        super(message);
    }
}