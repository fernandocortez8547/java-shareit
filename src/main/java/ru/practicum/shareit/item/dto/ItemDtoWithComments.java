package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

@Data
public class ItemDtoWithComments {
    private long id;
    private String name;
    private String description;
    private Collection<Comment> comments;
    private Booking lastBooking;
    private Booking nextBooking;
}
