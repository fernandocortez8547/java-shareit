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
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;

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
        User user = userService.findUser(userId);
        Item item = itemMapper.getItem(userId, itemDto);
        item.setOwner(user);
        return itemMapper.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
        if (item.getOwner().getId() != userId) {
            throw new IncorrectOwnerException("Owner can't be changed.");
        }
        item.setOwner(item.getOwner());
        item = itemMapper.getItem(itemDto, item);
        return itemMapper.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
        Collection<Comment> comments = commentRepository.findAllByItemId(itemId);
        Collection<Booking> itemBookings = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusNot(itemId, userId, REJECTED);
        return itemMapper.getItemDto(item, itemBookings, comments);
    }

    @Override
    public Collection<ItemDto> getUserItemsWithBooking(long userId) {
        Collection<Item> items = itemRepository.findByOwnerId(userId);
        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerId(userId);
        Collection<Comment> comments = commentRepository.findAllByUserIdOrItemOwnerId(userId, userId);
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
        return itemRepository.searchByNameOrDescription(text).stream().map(itemMapper::getItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long itemId, long userId, Comment comment) {
        Collection<Booking> bookings = bookingRepository.findByBookerIdAndItemIdAndStatusNot(userId, itemId, REJECTED);
        if (bookings.isEmpty()) {
            throw new BadRequestException("Bad request.");
        }
        comment.setItem(itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new));
        comment.setUser(userService.findUser(userId));
        comment.setCreated(LocalDateTime.now());
        return itemMapper.getCommentDto(commentRepository.save(comment));
    }

    @Override
    public void removeItem(long id) {
        itemRepository.deleteById(id);
    }
}
