package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid BookingRequestDto bookingDto) {
        log.info("Received request: path=/bookings, http-method=POST, X-Sharer-User-Id={}", userId);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public BookingResponseDto updateBooking(@PathVariable("id") long id,
                                            @RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(value = "approved") boolean approved) {
        log.info("Received request: path=/bookings/{}?approved={}, http-method=PATCH, X-Sharer-User-Id={}", id, approved, userId);
        return bookingService.updateBooking(id, userId, approved);
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBookingById(@PathVariable long id,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received request: path=/bookings/{}, http-method=GET, X-Sharer-User-Id={}",id, userId);
        return bookingService.getBookingById(id, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Received request: path=/bookings/owner?state={}, http-method=GET, X-Sharer-User-Id={}", state, userId);
        return bookingService.getBookingsByUserId(userId, state, true);
    }

    @GetMapping
    public Collection<BookingResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Received request: path=/bookings?state={}, http-method=GET, X-Sharer-User-Id={}", state, userId);
        return bookingService.getBookingsByUserId(userId, state, false);
    }

    @DeleteMapping("{id}")
    public void removeBooking(@PathVariable long id) {
        log.info("Received request: path=/bookings/{}, http-method=DELETE", id);
        bookingService.removeBooking(id);
    }
}