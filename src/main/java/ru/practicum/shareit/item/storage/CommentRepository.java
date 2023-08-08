package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByItemId(@Param("itemId") long itemId);

    Collection<Comment> findAllByUserIdOrItemOwner(@Param("userId") long userId, @Param("itemOwner") long itemOwner);
}
