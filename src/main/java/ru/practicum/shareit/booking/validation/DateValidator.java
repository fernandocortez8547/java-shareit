package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateTimeRange, BookingRequestDto> {

    @Override
    public void initialize(DateTimeRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingRequestDto booking, ConstraintValidatorContext context) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (end == null || start == null) {
            return false;
        }
        if (start.equals(end) || start.isAfter(end)) {
            return false;
        }
        return !start.isBefore(LocalDateTime.now());
    }
}
