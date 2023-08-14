package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.validation.DateTimeRange;

import java.time.LocalDateTime;

@Data
@DateTimeRange
public class BookingRequestDto {
    private long id;
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}