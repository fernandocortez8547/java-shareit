package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item addItem(Item item);
    Item updateItem(long itemId, Item item);
    Item getItem(long id);
    Collection<Item> getUserItems(long owner);
    Collection<Item> getAllItems();
    void removeItem(long id);
}
