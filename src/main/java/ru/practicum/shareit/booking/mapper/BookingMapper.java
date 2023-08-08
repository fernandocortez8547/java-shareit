package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;

    public Booking getBooking(User booker, Item item, BookingRequestDto bookingDto) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        return booking;
    }

    public BookingResponseDto getBookingDto(Booking booking) {
        BookingResponseDto bookingDto = new BookingResponseDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(itemMapper.getItemDto(booking.getItem()));
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        return bookingDto;
    }
}
