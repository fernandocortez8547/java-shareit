package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private final long id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;
    @NotNull(message = "Must be filled.")
    private final Boolean available;
}
