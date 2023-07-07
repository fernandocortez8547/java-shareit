package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);
    User updateUser(long userId, User user);
    User getUser(long userId);
    Collection<User> getUsers();
    void removeUser(long userId);
}
