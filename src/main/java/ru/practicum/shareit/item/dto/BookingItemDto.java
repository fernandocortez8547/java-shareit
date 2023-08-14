package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class BookingItemDto {
    private final long id;
    private final String name;
    private final String description;
}