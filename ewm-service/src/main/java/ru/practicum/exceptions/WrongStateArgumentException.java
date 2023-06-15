package ru.practicum.exceptions;

public class WrongStateArgumentException extends RuntimeException {

    public WrongStateArgumentException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
