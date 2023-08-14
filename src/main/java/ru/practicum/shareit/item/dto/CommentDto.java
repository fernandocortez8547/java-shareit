package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private final long id;
    private final long itemId;
    private final String authorName;
    private final String text;
    private final LocalDateTime created;
}
