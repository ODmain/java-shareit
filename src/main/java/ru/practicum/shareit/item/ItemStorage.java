package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {
    ItemDto addItem(Long userId, Item item);

    ItemDto updateItem(Item item, Long userId, Long itemId);

    ItemDto getItem(Long itemId);

    List<ItemDto> getItemsOfOwner(Long userId);

    List<ItemDto> searchForTheItem(String text);

    void deleteItem(Long itemId);

    boolean existsItem(Long itemId);

}
