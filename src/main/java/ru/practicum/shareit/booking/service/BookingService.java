package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.UnauthorizedAccessException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static ru.practicum.shareit.booking.BookingStatus.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingResponseDto addBooking(long userId, BookingRequestDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("Item not found."));
        if (!item.getAvailable()) {
            throw new UnauthorizedAccessException("You don't have access to item");
        } else if (item.getOwner() == userId) {
            throw new ItemNotFoundException("Not found.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        Booking booking = bookingMapper.getBooking(user, item, bookingDto);
        booking.setStatus(WAITING);
        return bookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    public BookingResponseDto updateBooking(long bookingId, long userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + bookingId + " not found."));
        BookingStatus status = booking.getStatus();
        if (userId != booking.getItem().getOwner() || status.equals(APPROVED)) {
            if (userId == booking.getBooker().getId()) {
                throw new ItemNotFoundException("Item not found.");
            }
            throw new UnauthorizedAccessException("You don't have access to item");
        }
        if (booking.getStatus().equals(WAITING)) {
            if (approved) {
                booking.setStatus(APPROVED);
            } else {
                booking.setStatus(REJECTED);
            }
        } else {
            throw new BookingNotFoundException("Booking not found.");
        }
        return bookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    public BookingResponseDto getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found."));
        if (userId != booking.getItem().getOwner() && userId != booking.getBooker().getId()) {
            throw new BookingNotFoundException("You don't have access to booking");
        }
        return bookingMapper.getBookingDto(booking);
    }

    public Collection<BookingResponseDto> getBookingsByUserId(long userId, String status, boolean isOwner) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        long bookerId = isOwner ? 0 : userId;
        userId = !isOwner ? 0 : userId;
        return getBookingByStatus(bookerId, userId, status).stream().map(bookingMapper::getBookingDto).collect(Collectors.toList());
    }

    private Collection<Booking> getBookingByStatus(long bookerId, long userId, String status) {
        switch (status) {
            case "ALL":
                return bookingRepository.findAllByBookerIdOrItemOwnerId(bookerId, userId);
            case "CURRENT":
                return bookingRepository.findAllByTimeBetween(bookerId, userId);
            case "PAST":
                return bookingRepository.findAllByEndBefore(bookerId, userId);
            case "FUTURE":
                return bookingRepository.findAllByStartAfter(bookerId, userId);
            case "WAITING":
                log.debug(WAITING + " log");
                return bookingRepository.findAllByStatusAndBookerIdOrItemOwner(bookerId, userId, WAITING);
            case "REJECTED":
                log.debug(REJECTED + " log");
                return bookingRepository.findAllByStatusAndBookerIdOrItemOwner(bookerId, userId, REJECTED);
            case "APPROVED":
                log.debug(APPROVED + " log");
                return bookingRepository.findAllByStatusAndBookerIdOrItemOwner(bookerId, userId, APPROVED);
            default:
                throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public void removeBooking(long id) {
        bookingRepository.deleteById(id);
    }
}
