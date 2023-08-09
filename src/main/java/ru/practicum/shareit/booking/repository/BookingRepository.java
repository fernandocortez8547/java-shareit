package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId OR b.item.owner = :ownerId ORDER BY b.start DESC")
    Collection<Booking> findByBookerIdOrItemOwner(long bookerId, long ownerId);

    @Query("SELECT b FROM Booking b WHERE (b.booker.id = :bookerId OR b.item.owner = :ownerId) AND b.end < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    Collection<Booking> findByBookingByEndInPast(long bookerId, long ownerId);

    @Query("SELECT b FROM Booking b WHERE (b.booker.id = :bookerId OR b.item.owner = :ownerId) AND b.start > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    Collection<Booking> findByBookingByEndInFuture(long bookerId, long ownerId);

    @Query("SELECT b FROM Booking b WHERE (b.booker.id = :bookerId OR b.item.owner = :ownerId) " +
            "AND (b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP) " +
            "ORDER BY b.start DESC")
    Collection<Booking> findAllByTimeBetween(long bookerId, long ownerId);

    @Query("select b from Booking b where (b.booker.id = :bookerId or b.item.owner = :owner) AND b.status = :status")
    Collection<Booking> findAllByStatusAndBookerIdOrItemOwner(
            long bookerId, long owner, BookingStatus status);

    Collection<Booking> findAllByItemOwner(@Param("owner") long owner);


    Collection<Booking> findAllByItemIdAndItemOwnerAndStatusNot(@Param("id") long itemId, @Param("owner") long owner, BookingStatus status);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.item.id = :itemId AND b.status <> :status AND b.start < CURRENT_TIMESTAMP")
    Collection<Booking> findByBookerIdAndItemIdAndStatusNot(@Param("bookerId") long bookerId, @Param("itemId") long itemId, BookingStatus status);
}
