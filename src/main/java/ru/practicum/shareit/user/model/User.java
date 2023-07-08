package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"id", "name"})
public class User {
    private long id = 0;
    private String name;
    private String email;
}
