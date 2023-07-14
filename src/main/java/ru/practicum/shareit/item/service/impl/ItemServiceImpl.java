package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item addItem(long userId, Item item) {
        userService.getUser(userId);
        item.setOwner(userId);
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        userService.getUser(userId);
        item.setOwner(userId);
        return itemStorage.updateItem(itemId, item);
    }

    @Override
    public Item getItem(long itemId) {
        return itemStorage.getItem(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long userId) {
        userService.getUser(userId);
        return itemStorage.getUserItems(userId);
    }

    @Override
    public Collection<Item> getAllItems() {
        return itemStorage.getAllItems();
    }

    @Override
    public Collection<Item> searchItem(String text) {
        return itemStorage.searchItems(text.toLowerCase());
    }

    @Override
    public void removeItem(long id) {
        itemStorage.removeItem(id);
    }
}
