package ru.practicum.shareit.item.exception;

public class IncorrectOwnerException extends RuntimeException {
    public IncorrectOwnerException (String message) {
        super(message);
    }
}
