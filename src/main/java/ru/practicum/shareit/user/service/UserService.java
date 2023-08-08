package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto addUser(UserDto userDto) {
        User user = userMapper.getUser(userDto);
        return userMapper.getUserDto(userRepository.save(user));
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow();
        user = userMapper.getUser(userDto, user);
        return userMapper.getUserDto(userRepository.save(user));
    }

    public UserDto getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id " + id + " not found.")
        );
        return userMapper.getUserDto(user);
    }

    public User findUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + "not found."));
    }

    public Collection<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    public void removeUser(long id) {
        userRepository.deleteById(id);
    }
}
