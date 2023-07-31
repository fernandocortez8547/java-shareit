package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    private long id = 0;
    private long owner = 0;
    private String name;
    private String description;
    private Boolean available;
}
