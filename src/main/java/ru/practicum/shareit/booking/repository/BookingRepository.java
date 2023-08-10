package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Optional<Booking> findByIdAndItemOwnerId(long id, long owner);

    Collection<Booking> findAllByItemOwnerId(long itemOwner);

    Collection<Booking> findAllByItemIdAndItemOwnerIdAndStatusNot(long itemId, long owner, BookingStatus status);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.item.id = :itemId AND b.status <> :status AND b.start < CURRENT_TIMESTAMP")
    Collection<Booking> findByBookerIdAndItemIdAndStatusNot(long bookerId, long itemId, BookingStatus status);
}
