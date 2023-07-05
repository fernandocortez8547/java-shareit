package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final ItemService itemService;

    public ItemDto getItemDto(Item item) {
        return new ItemDto(item.getId(), item.getOwner(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item getItem(long id, ItemDto itemDto) {
        Item item = new Item();
        item.setId(id);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}
