package ru.practicum.shareit.item.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findByOwnerId(long userId);

    @Query("select i from Item i where (lower(i.name) LIKE lower(CONCAT('%', ?1, '%'))" +
            " OR lower(i.description) LIKE lower(concat('%', ?1, '%'))) " +
            "AND i.available = true")
    Collection<Item> searchByNameOrDescription(String text);
}
