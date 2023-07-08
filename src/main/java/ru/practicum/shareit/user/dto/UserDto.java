package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private final long id;
    private final String name;
    @Email
    @NotBlank
    private final String email;
}
