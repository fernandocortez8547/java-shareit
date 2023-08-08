package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

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
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id, @RequestBody ItemDto itemDto) {
        log.info("Received request: path=/item/" + id + ", http-method=PATCH");
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("{id}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Received request: path=/items/{}, http-method=GET, X-Sharer-User-Id={}", id, userId);
        return itemService.getItem(userId, id);
    }

    @GetMapping
    public Collection<ItemDto> getUserItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        Collection<ItemDto> items;
        if (userId != null) {
            log.info("Received request: path=/item, http-method=GET, X-Sharer-User-Id=" + userId);
            return itemService.getUserItemsWithBooking(userId);
        } else {
            log.info("Received request: path=/item, http-method=GET");
            items = itemService.getAllItems();
        }

        return items;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam("text") String text) {
        log.info("Received request: path=/item/search?text={}, http-method=GET", text);
        return itemService.searchItem(text);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@PathVariable("id") long itemId, @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid Comment comment) {
        log.info("Received request: path=/items/{}/comment, http-method=POST X-Sharer-User-Id={}", itemId, userId);
        return itemService.addComment(itemId, userId, comment);
    }

    @DeleteMapping
    public void removeItem(long id) {
        itemService.removeItem(id);
    }
}