package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Received request: path=/item, http-method=POST");
        Item item = itemMapper.getItem(userId, itemDto);
        return itemMapper.getItemDto(itemService.addItem(userId, item));
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id, @RequestBody ItemDto itemDto) {
        log.info("Received request: path=/item/" + id + ", http-method=PATCH");
        Item item = itemMapper.getItem(userId, itemDto);
        return itemMapper.getItemDto(itemService.updateItem(userId, id, item));
    }

    @GetMapping("{id}")
    public ItemDto getItem(@PathVariable long id) {
        log.info("Received request: path=/item/" + id + ", http-method=GET");
        return itemMapper.getItemDto(itemService.getItem(id));
    }

    @GetMapping
    public Collection<ItemDto> getUserItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        Collection<Item> items;
        if (userId != null) {
            log.info("Received request: path=/item, http-method=GET, X-Sharer-User-Id=" + userId);
            items = itemService.getUserItems(userId);
        } else {
            log.info("Received request: path=/item, http-method=GET");
            items = itemService.getAllItems();
        }

        return items.stream().map(itemMapper::getItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<Item> searchItem(@RequestParam("text") String text) {
        log.info("Received request: path=/item/search?text=" + text + ", http-method=GET");
        return itemService.searchItem(text);
    }

    @DeleteMapping
    public void removeItem(long id) {
        itemService.removeItem(id);
    }
}