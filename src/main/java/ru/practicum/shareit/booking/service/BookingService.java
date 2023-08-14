package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        if (!item.getAvailable()) {
            throw new AccessDeniedException("You don't have access to item");
        } else if (item.getOwner().getId() == userId) {
            throw new ItemNotFoundException();
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Booking booking = bookingMapper.getBooking(user, item, bookingDto);
        return bookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    public BookingResponseDto updateBooking(long bookingId, long userId, boolean approved) {
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(BookingNotFoundException::new);
        if (userId != booking.getItem().getOwner().getId() || booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new AccessDeniedException("You don't have access to item");
        } else if (booking.getStatus().equals(BookingStatus.WAITING)) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            throw new BookingNotFoundException();
        }
        return bookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    public BookingResponseDto getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new BookingNotFoundException();
        }
        return bookingMapper.getBookingDto(booking);
    }

    public Collection<BookingResponseDto> getBookingsByUserId(long userId, String status, boolean isOwner) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Specification<Booking> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate p;
            if (isOwner) {
                p = criteriaBuilder.equal(root.join("item").get("owner").get("id"), userId);
            } else {
                p = criteriaBuilder.equal(root.join("booker").get("id"), userId);
            }
            predicates.add(p);
            Predicate predicate = null;
            switch (status) {
                case "ALL":
                    break;
                case "CURRENT":
                    Predicate startLessThanNow = criteriaBuilder.lessThan(root.get("start"), LocalDateTime.now());
                    Predicate endGreaterThanNow = criteriaBuilder.greaterThan(root.get("end"), LocalDateTime.now());
                    predicate = criteriaBuilder.and(startLessThanNow, endGreaterThanNow);
                    break;
                case "PAST":
                    predicate = (criteriaBuilder.lessThan(root.get("end"), LocalDateTime.now()));
                    break;
                case "FUTURE":
                    predicate = (criteriaBuilder.greaterThan(root.get("start"), LocalDateTime.now()));
                    break;
                case "WAITING":
                    predicate = (criteriaBuilder.equal(root.get("status"), BookingStatus.WAITING));
                    break;
                case "APPROVED":
                    predicate = (criteriaBuilder.equal(root.get("status"), BookingStatus.APPROVED));
                    break;
                case "REJECTED":
                    predicate = (criteriaBuilder.equal(root.get("status"), BookingStatus.REJECTED));
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (predicate != null) {
               predicates.add(predicate);
            }
            query.orderBy(criteriaBuilder.desc(root.get("start")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return bookingRepository.findAll(specification).stream().map(bookingMapper::getBookingDto).collect(Collectors.toList());
    }

    public void removeBooking(long id) {
        bookingRepository.deleteById(id);
    }
}
