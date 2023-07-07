package ru.practicum.shareit.item.storage.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImpl implements ItemStorage {
    private final static Logger log = (Logger) LoggerFactory.getLogger(ItemStorageImpl.class);
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    public ItemStorageImpl() {
        log.setLevel(Level.DEBUG);
    }

    @Override
    public Item addItem(Item item) {
        item.setId(idGeneration());
        log.info("Save item with id={}.", item.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item updateItem) {
        if (!items.containsKey(updateItem.getId())) {
            log.warn("Item with id={} not found", updateItem.getId());
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }

        Item item = items.get(updateItem.getId());
        String name = updateItem.getName();
        String description = updateItem.getDescription();
        Boolean available = updateItem.getAvailable();
        long owner = updateItem.getOwner();
        log.debug("available {}",available);

        if (item.getOwner() != updateItem.getOwner()) {
            log.debug("Set item owner id to {}.", item.getOwner());
            if (owner == 0) {
                updateItem.setOwner(item.getOwner());
            } else {
                log.warn("Attempt to change owner id on {}, instead of {}", owner, item.getId());
                throw new IncorrectOwnerException("Owner can't be changed.");
            }
        }
        if (name == null || name.isBlank()) {
            log.debug("Set item name to {}.", item.getName());
            updateItem.setName(item.getName());
        }
        if (description == null || description.isBlank()) {
            log.debug("Set item description to {}.", item.getDescription());
            updateItem.setDescription(item.getDescription());
        }
        if (available == null) {
            log.debug("Set item available to {}.", item.getAvailable());
            updateItem.setAvailable(item.getAvailable());
        }
        log.info("Update item with id={}.", item.getId());
        items.put(updateItem.getId(), updateItem);

        return updateItem;
    }

    @Override
    public Item getItem(long itemId) {
        if(!items.containsKey(itemId)) {
            log.warn("Item with id={} not found", itemId);
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }
        log.info("Get item with id={}.", itemId);
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long owner) {
        log.info("Get all items with owner={}.", owner);
        return items.values().stream().filter(item -> item.getOwner() == owner).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getAllItems() {
        log.info("Get all items.");
        return items.values();
    }

    @Override
    public void removeItem(long id) {
        if(!items.containsKey(id)) {
            log.warn("Item with id={} not found", id);
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }
        log.info("Remove item with id={}", id);
        items.remove(id);
    }

    private long idGeneration() {
        return ++id;
    }
}
