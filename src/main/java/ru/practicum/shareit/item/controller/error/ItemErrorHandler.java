package ru.practicum.shareit.item.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {ItemController.class})
public class ItemErrorHandler {
    @ExceptionHandler({IncorrectOwnerException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> incorrectDataHandler(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({ItemNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundHandler(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}
