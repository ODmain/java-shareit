package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.UserMapper.fromUserDto;

@Repository
@RequiredArgsConstructor
public class ItemDbStorage implements ItemStorage {

    HashMap<Long, Item> items = new HashMap<>();

    private final UserStorage userStorage;

    private long id = 0;

    private Item setItemId(Item item) {
        item.setId(++id);
        return item;
    }

    @Override
    public ItemDto addItem(Long userId, Item item) {
        Item it = setItemId(item);
        UserDto user = userStorage.getUser(userId);
        it.setOwner(fromUserDto(user));
        items.put(it.getId(), it);
        return toItemDto(it);
    }

    @Override
    public ItemDto updateItem(Item item, Long userId, Long itemId) {
        if (item.getName() != null) {
            items.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(itemId).setDescription(item.getDescription());
        }
        if (item.available != null) {
            if (item.available != items.get(itemId).available) {
                items.get(itemId).available = item.available;
            }
        }
        return toItemDto(items.get(itemId));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return toItemDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsOfOwner(Long userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId().equals(userId)) {
                itemsDto.add(toItemDto(item));
            }
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> searchForTheItem(String text) {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : items.values()) {
            if (!(text.isEmpty() && text.isBlank()) && item.available) {
                if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    itemDto.add(toItemDto(item));
                }
            }
        }
        return itemDto;
    }

    @Override
    public void deleteItem(Long itemId) {
        items.remove(itemId);
    }

    @Override
    public boolean existsItem(Long itemId) {
        return items.containsKey(itemId);
    }
}
