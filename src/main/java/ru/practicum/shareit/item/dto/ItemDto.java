package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.ItemBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class ItemDto {
    private final long id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;
    @NotNull(message = "Must be filled.")
    private final Boolean available;
    private Collection<CommentDto> comments;
    private ItemBookingDto lastBooking;
    private ItemBookingDto nextBooking;
}
