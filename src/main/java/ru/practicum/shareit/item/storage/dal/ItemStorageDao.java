package ru.practicum.shareit.item.storage.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collection;

@Repository("ItemDbStorage")
@RequiredArgsConstructor
public class ItemStorageDao implements ItemStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Item addItem(Item item) {
        return null;
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        return null;
    }

    @Override
    public Item getItem(long id) {
        return null;
    }

    @Override
    public Collection<Item> getUserItems(long owner) {
        return null;
    }

    @Override
    public Collection<Item> getAllItems() {
        return null;
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return null;
    }

    @Override
    public void removeItem(long id) {

    }
}
