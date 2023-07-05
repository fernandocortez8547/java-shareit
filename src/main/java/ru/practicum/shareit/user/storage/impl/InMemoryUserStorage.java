package ru.practicum.shareit.user.storage.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exception.EmailAlreadyExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailSet = new HashSet<>();
    private long id = 0;

    @Override
    public User addUser(User user) {
        if(emailSet.contains(user.getEmail())) {
            throw new EmailAlreadyExistException("User with email " + user.getEmail() + " is already exist.");
        }
        user.setId(idGeneration());
        emailSet.add(user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long userId, User updateUser) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }

        User user = users.get(userId);
        String email = updateUser.getEmail();
        String name = updateUser.getName();

        if (email == null || user.getEmail().equals(email)) {
            updateUser.setEmail(user.getEmail());
        } else if (emailSet.contains(email)) {
            throw new EmailAlreadyExistException("User with email " + updateUser.getEmail() + " is already exist.");
        } else {
            emailSet.remove(user.getEmail());
            emailSet.add(email);
        }

        if (name == null) {
            updateUser.setName(user.getName());
        }

        users.put(userId, updateUser);
        return updateUser;
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void removeUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        emailSet.remove(users.get(id).getEmail());
        users.remove(id);
    }

    private long idGeneration() {
        return ++id;
    }
}
