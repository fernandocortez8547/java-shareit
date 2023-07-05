package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(exclude= {"id", "name"})
public class User {
    private long id = 0;
    private String name;
    @Email
    @NotBlank
    private String email;
}
