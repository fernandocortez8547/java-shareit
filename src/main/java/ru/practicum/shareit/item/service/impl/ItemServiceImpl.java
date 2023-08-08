package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.BadRequestException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingStatus.REJECTED;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;


    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        userService.getUser(userId);
        Item item = itemMapper.getItem(userId, itemDto);
        item.setOwner(userId);
        return itemMapper.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found."));
        if (item.getOwner() != userId) {
            throw new IncorrectOwnerException("Owner can't be changed.");
        }
        item = itemMapper.getItem(itemDto, item);
        return itemMapper.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found."));
        Collection<Comment> comments = commentRepository.findAllByItemId(itemId);
        Collection<Booking> itemBookings = bookingRepository.findAllByItemIdAndItemOwnerAndStatusNot(itemId, userId, REJECTED);
        if (userId != item.getOwner()) {
            return itemMapper.getItemDto(item, itemBookings, comments);
        }
        log.info(itemBookings.toString());
        return itemMapper.getItemDto(item, itemBookings, comments);
    }

    @Override
    public Collection<ItemDto> getUserItemsWithBooking(long userId) {
        Collection<Item> items = itemRepository.findByOwner(userId);
        Collection<Booking> bookings = bookingRepository.findAllByItemOwner(userId);
        Collection<Comment> comments = commentRepository.findAllByUserIdOrItemOwner(userId, userId);
        Map<Item, List<Booking>> bookingsByOwner = bookings.stream()
                .collect(Collectors.groupingBy(
                        Booking::getItem,
                        Collectors.toList()
                ));
        Map<Item, List<Comment>> commentsByOwner = comments.stream()
                .collect(Collectors.groupingBy(
                        Comment::getItem,
                        Collectors.toList()
                ));

        return items.stream()
                .map(i -> itemMapper.getItemDto(i, bookingsByOwner.get(i), commentsByOwner.get(i)))
                .sorted(Comparator.comparing(i -> (i.getLastBooking() == null && i.getNextBooking() == null)))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> getAllItems() {
        return itemRepository.findAll().stream().map(itemMapper::getItemDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(text).stream().map(itemMapper::getItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long itemId, long userId, Comment comment) {
        Collection<Booking> bookings = bookingRepository.findByBookerIdAndItemIdAndStatusNot(userId, itemId, REJECTED);
        if (bookings.isEmpty()) {
            throw new BadRequestException("Bad request.");
        }
        log.info(bookings.toString());
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found."));
        User user = userService.findUser(userId);
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        return itemMapper.getCommentDto(commentRepository.save(comment));
    }

    @Override
    public void removeItem(long id) {
        itemRepository.deleteById(id);
    }
}
