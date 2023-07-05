package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private final long id;
    private final long owner;
    private final String name;
    private final String description;
    private final Boolean available;
}
