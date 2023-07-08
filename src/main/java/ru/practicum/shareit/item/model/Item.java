package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {
    private long id = 0;
    private long owner = 0;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull()
    private Boolean available;
}
