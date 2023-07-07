package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item addItem(long userId, Item item);
    Item updateItem(long userId, Item item);
    Item getItem(long itemId);
    Collection<Item> getUserItems(long userId);
    Collection<Item> getAllItems();
    Collection<Item> searchItem(String text);
    void removeItem(long id);
}
