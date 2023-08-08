package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItem(long userId, long itemId);

    Collection<ItemDto> getAllItems();

    Collection<ItemDto> searchItem(String text);

    CommentDto addComment(long itemId, long userId, Comment comment);

    void removeItem(long id);

    Collection<ItemDto> getUserItemsWithBooking(long userId);
}
