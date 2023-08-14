package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "text", nullable = false)
    @NotBlank(message = "text can not be empty")
    private String text;
    @Column(name = "created")
    private LocalDateTime created;
}
