package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item addItem(long userId, Item item) {
        userService.getUser(userId);
        item.setOwner(userId);
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(long userId, Item item) {
        userService.getUser(userId);
        item.setOwner(userId);
        return itemStorage.updateItem(item);
    }

    @Override
    public Item getItem(long itemId) {
        return itemStorage.getItem(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long userId) {
        userService.getUser(userId);
        log.info("Get all items with owner={}.", userId);
        return itemStorage.getAllItems().stream().filter(item -> item.getOwner() == userId).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getAllItems() {
        return itemStorage.getAllItems();
    }

    @Override
    public Collection<Item> searchItem(String text) {
        if (text.isBlank()) {
            log.info("Get empty list. Text for search is blank.");
            return Collections.emptyList();
        }
        log.info("Get items where name or description contains {}", text);
        return itemStorage.getAllItems().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(long id) {
        itemStorage.removeItem(id);
    }
}
