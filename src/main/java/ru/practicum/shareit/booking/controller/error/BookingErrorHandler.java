package ru.practicum.shareit.booking.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {BookingController.class})
public class BookingErrorHandler {
    @ExceptionHandler({UnsupportedOperationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> incorrectDataHandler(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({ItemNotFoundException.class, UserNotFoundException.class, BookingNotFoundException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundHandler(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequestHandler(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> internalServerErrorHandler(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}