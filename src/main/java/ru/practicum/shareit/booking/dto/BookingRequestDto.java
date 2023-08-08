package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    private long id;
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status = BookingStatus.WAITING;
}