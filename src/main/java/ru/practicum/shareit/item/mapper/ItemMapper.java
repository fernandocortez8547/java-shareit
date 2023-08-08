package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentMapper commentMapper;

    public ItemDto getItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item getItem(long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setOwner(userId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public Item getItem(ItemDto itemDto, Item item) {
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name != null && !name.isBlank()) {
            item.setName(itemDto.getName());
        }
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        if (available != null) {
            item.setAvailable(available);
        }
        return item;
    }

    public ItemDto getItemDto(Item item, Collection<Booking> bookings, Collection<Comment> comments) {
        ItemDto itemDto =  new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
        if (bookings == null)
            return itemDto;

        if (comments == null)
            itemDto.setComments(Collections.emptyList());
        else
            itemDto.setComments(comments.stream().map(commentMapper::toCommentDto).collect(Collectors.toList()));
        itemDto.setLastBooking(getLastBooking(bookings));
        itemDto.setNextBooking(getNextBooking(bookings));

        return itemDto;
    }

    public CommentDto getCommentDto(Comment comment) {
        return commentMapper.toCommentDto(comment);
    }

    private BookingDto getNextBooking(Collection<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .map(b -> new BookingDto(b.getId(), b.getBooker().getId()))
                .orElse(null);
    }

    private BookingDto getLastBooking(Collection<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart))
                .map(b -> new BookingDto(b.getId(), b.getBooker().getId()))
                .orElse(null);
    }
}
