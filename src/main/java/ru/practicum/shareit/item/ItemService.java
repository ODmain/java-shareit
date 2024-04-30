package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.fromItemDto;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto addItem(Long userId, ItemDto itemDto) {
        if (userId == null || itemDto == null || itemDto.available == null) {
            throw new ValidException("Переданы неверные данные", HttpStatus.BAD_REQUEST);
        }
        if (!userStorage.existsUser(userId)) {
            throw new ValidException("Такого пользователя нет", HttpStatus.NOT_FOUND);
        }
        return itemStorage.addItem(userId, fromItemDto(itemDto));
    }

    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        if (userId == null || itemDto == null) {
            throw new ValidException("Переданы неверные данные", HttpStatus.BAD_REQUEST);
        }
        if (!itemStorage.existsItem(itemId)) {
            throw new ValidException("Предмета с таким id не существует", HttpStatus.NOT_FOUND);
        }
        if (!userId.equals(itemStorage.getItem(itemId).getOwner().getId())) {
            throw new ValidException("Вы не имеете права редактировать данный предмет", HttpStatus.FORBIDDEN);
        }
        return itemStorage.updateItem(fromItemDto(itemDto), userId, itemId);
    }

    public ItemDto getItem(Long itemId) {
        if (!itemStorage.existsItem(itemId)) {
            throw new ValidException("Предмета с таким id не существует", HttpStatus.BAD_REQUEST);
        }
        return itemStorage.getItem(itemId);
    }

    public List<ItemDto> getItemsOfOwner(Long userId) {
        if (userId == null) {
            throw new ValidException("Переданы неверные данные", HttpStatus.BAD_REQUEST);
        }
        if (!userStorage.existsUser(userId)) {
            throw new ValidException("Такого пользователя нет", HttpStatus.NOT_FOUND);
        }
        return itemStorage.getItemsOfOwner(userId);
    }

    public List<ItemDto> searchForTheItem(String text) {
        return itemStorage.searchForTheItem(text);
    }

    public void deleteItem(Long userId, Long itemId) {
        if (userId == null) {
            throw new ValidException("Переданы неверные данные", HttpStatus.BAD_REQUEST);
        }
        if (itemStorage.existsItem(itemId)) {
            if (itemStorage.getItem(itemId).getOwner().getId().equals(userId)) {
                itemStorage.deleteItem(itemId);
            } else {
                throw new ValidException("Данный предмет зарегестрирован другим пользователем", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ValidException("Предмета с таким id не существует", HttpStatus.BAD_REQUEST);
        }
    }
}
