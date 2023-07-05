package ru.practicum.shareit.item.repository.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item addItem(Item item) {
        item.setId(idGeneration());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item updateItem) {
        if (!items.containsKey(updateItem.getId())) {
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }

        Item item = items.get(updateItem.getId());
        String name = updateItem.getName();
        String description = updateItem.getDescription();
        Boolean available = updateItem.getAvailable();
        long owner = updateItem.getOwner();

        if (items.get(updateItem.getId()).getOwner() != updateItem.getOwner()) {
            if (owner == 0) {
                updateItem.setOwner(item.getOwner());
            } else {
                throw new IncorrectOwnerException("Owner can't be changed.");
            }
        }
        if (name == null || name.isBlank()) {
            updateItem.setName(item.getName());
        }
        if (description == null || description.isBlank()) {
            updateItem.setDescription(item.getDescription());
        }
        if (available == null) {
            updateItem.setAvailable(item.getAvailable());
        }

        items.put(updateItem.getId(), updateItem);

        return updateItem;
    }

    private boolean isOwner(long itemId, long userId) {
        return items.get(itemId).getOwner() == userId;
    }

    @Override
    public Item getItem(long itemId) {
        if(!items.containsKey(itemId)) {
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }

        return items.get(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long owner) {
        return items.values().stream().filter(item -> item.getOwner() == owner).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getAllItems() {
        return items.values();
    }

    @Override
    public void removeItem(long id) {
        if(!items.containsKey(id)) {
            throw new ItemNotFoundException("Item with id " + id + " not found.");
        }

        items.remove(id);
    }

    private long idGeneration() {
        return ++id;
    }
}
