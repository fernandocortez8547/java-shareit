package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item addItem(Item item);
    Item updateItem(Item item);
    Item getItem(long id);
    Collection<Item> getUserItems(long owner);
    Collection<Item> getAllItems();
    void removeItem(long id);
}
