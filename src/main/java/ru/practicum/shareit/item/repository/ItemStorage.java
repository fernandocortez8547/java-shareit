package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item addItem(Item item);
    Item updateItem(Item item);
    Item getItem(long id);
    Collection<Item> getUserItems(long owner);
    Collection<Item> getAllItems();
    Collection<Item> searchItem(String text);
    void removeItem(long id);
}
