package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.model.Booking;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateTimeRange, Booking> {

    @Override
    public void initialize(DateTimeRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {
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
