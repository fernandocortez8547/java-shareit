package ru.practicum.shareit.booking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeRange {

    String message() default "Date is not validate.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
