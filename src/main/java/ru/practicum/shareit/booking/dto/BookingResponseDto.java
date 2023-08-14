package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private long id;
    private BookingItemDto item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}