package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        log.info("Received request: path=/users, http-method=POST");
        return userMapper.getUserDto(userService.addUser(user));
    }

    @PatchMapping("{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody User user) {
        log.info("Received request: path=/users/{}, http-method=PATCH", id);
        user = userService.updateUser(id, user);
        System.out.println(user.getId());
        return userMapper.getUserDto(user);
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable long id) {
        log.info("Received request: path=/users/{}, http-method=GET", id);
        return userMapper.getUserDto(userService.getUser(id));
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Received request: path=/users, http-method=GET");
        return userService.getUsers().stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("{id}")
    public void removeUser(@PathVariable long id) {
        log.info("Received request: path=/users/{}, http-method=DELETE", id);
        userService.removeUser(id);
    }
}
