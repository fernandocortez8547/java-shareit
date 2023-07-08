package ru.practicum.shareit.user.storage.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailAlreadyExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {
    public final Logger log = (Logger) LoggerFactory.getLogger(UserStorageImpl.class);
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailSet = new HashSet<>();
    private long id = 0;

    public UserStorageImpl() {
        log.setLevel(Level.DEBUG);
    }

    @Override
    public User addUser(User user) {
        if (emailSet.contains(user.getEmail())) {
            log.warn("User with email={} exist.", user.getEmail());
            throw new EmailAlreadyExistException("User with email " + user.getEmail() + " is already exist.");
        }
        user.setId(idGeneration());
        log.debug("Save user email={} to set.", user.getEmail());
        emailSet.add(user.getEmail());
        log.info("Save user with id={}.", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long userId, User updateUser) {
        if (!users.containsKey(userId)) {
            log.warn("User with id={} not found", userId);
            throw new UserNotFoundException("User with id " + id + " not found.");
        }

        User user = users.get(userId);
        String email = updateUser.getEmail();
        String name = updateUser.getName();
        updateUser.setId(userId);

        if (email == null || user.getEmail().equals(email)) {
            log.debug("Set user email to {}.", user.getEmail());
            updateUser.setEmail(user.getEmail());
        } else if (emailSet.contains(email)) {
            log.warn("User with email={} exist.", email);
            throw new EmailAlreadyExistException("User with email " + email + " is already exist.");
        } else {
            log.debug("Remove old user email {} from set.", user.getEmail());
            emailSet.remove(user.getEmail());
            log.debug("Set user email to {}.", updateUser.getEmail());
            emailSet.add(email);
        }

        if (name == null) {
            log.debug("Set user name to {}.", user.getName());
            updateUser.setName(user.getName());
        }

        log.info("Update user with id={}.", user.getId());
        users.put(userId, updateUser);
        return updateUser;
    }

    @Override
    public User getUser(long userId) {
        if (!users.containsKey(userId)) {
            log.warn("User with id={} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found.");
        }
        log.info("Get user with id={}.", userId);
        return users.get(userId);
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Get all users.");
        return users.values();
    }

    @Override
    public void removeUser(long userId) {
        if (!users.containsKey(userId)) {
            log.warn("User with id={} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found.");
        }
        String email = users.get(userId).getEmail();
        log.debug("Remove user email {} from set.", email);
        emailSet.remove(email);
        log.info("Remove user with id={}", userId);
        users.remove(userId);
    }

    private long idGeneration() {
        return ++id;
    }
}
