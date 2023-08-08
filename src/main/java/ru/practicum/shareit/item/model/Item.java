package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "owner")
    private long owner;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "last_booking_id")
    private Booking lastBooking;
    @ManyToOne
    @JoinColumn(name = "next_booking_id")
    private Booking nextBooking;
}
