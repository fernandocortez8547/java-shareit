package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody Item item) {
        return itemMapper.getItemDto(itemService.addItem(userId, item));
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id, @RequestBody ItemDto itemDto) {
        Item item = itemMapper.getItem(id, itemDto);
        return itemMapper.getItemDto(itemService.updateItem(userId, item));
    }

    @GetMapping("{id}")
    public ItemDto getItem(@PathVariable long id) {
        return itemMapper.getItemDto(itemService.getItem(id));
    }

    @GetMapping
    public Collection<ItemDto> getUserItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        Collection<Item> items;
        if (userId != null)
            items = itemService.getUserItems(userId);
        else
            items = itemService.getAllItems();

        return items.stream().map(itemMapper::getItemDto).collect(Collectors.toList());
    }

    @DeleteMapping
    public void removeItem(long id) {
        itemService.removeItem(id);
    }
}