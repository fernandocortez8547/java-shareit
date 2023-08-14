package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received request: path=/users, http-method=POST");
        return userService.addUser(userDto);
    }

    @PatchMapping("{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        log.info("Received request: path=/users/{}, http-method=PATCH", id);
        return userService.updateUser(id, userDto);
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable long id) {
        log.info("Received request: path=/users/{}, http-method=GET", id);
        return userService.getUser(id);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Received request: path=/users, http-method=GET");
        return userService.getUsers();
    }

    @DeleteMapping("{id}")
    public void removeUser(@PathVariable long id) {
        log.info("Received request: path=/users/{}, http-method=DELETE", id);
        userService.removeUser(id);
    }
}
