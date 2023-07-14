package ru.practicum.shareit.item.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItems = new HashMap<>();
    private long id = 0;

    @Override
    public Item addItem(Item item) {
        item.setId(idGeneration());
        long userId = item.getOwner();
        log.info("Save to itemUsers with owner={}", userId);
        if (userItems.containsKey(userId)) {
            List<Item> userItemsValues = userItems.get(userId);
            userItemsValues.add(item);
            userItems.put(userId, userItemsValues);
        } else {
            userItems.put(userId, new ArrayList<>(List.of(item)));
        }
        log.info("Save item with id={}.", item.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item updateItem) {
        if (!items.containsKey(itemId)) {
            log.warn("Item with id={} not found", updateItem.getId());
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }

        Item item = items.get(itemId);
        String name = updateItem.getName();
        String description = updateItem.getDescription();
        Boolean available = updateItem.getAvailable();
        long owner = updateItem.getOwner();
        log.debug("owner {}", owner);
        log.debug("available {}",available);
        updateItem.setId(itemId);

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
        log.debug("Over writing item {} in userItems.", item.getId());
        List<Item> userItemsValues = userItems.get(owner);
        userItemsValues.remove(item);
        userItemsValues.add(updateItem);
        userItems.put(owner, userItemsValues);
        log.info("Update item with id={}.", item.getId());
        items.put(updateItem.getId(), updateItem);
        return updateItem;
    }

    @Override
    public Item getItem(long itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("Item with id={} not found", itemId);
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }
        log.info("Get item with id={}.", itemId);
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long owner) {
        log.info("Get all items with owner={}.", owner);
        return userItems.get(owner);
    }

    @Override
    public Collection<Item> getAllItems() {
        log.info("Get all items.");
        return items.values();
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("Get items where name or description contains {}", text);
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(long id) {
        log.debug("Remove item={} from userItem.", id);
        Item item = items.get(id);
        List<Item> userItemsValues = userItems.get(item.getOwner());
        userItemsValues.remove(item);
        userItems.put(item.getOwner(), userItemsValues);
        log.info("Remove item with id={}", id);
        items.remove(id);
    }

    private long idGeneration() {
        return ++id;
    }
}
