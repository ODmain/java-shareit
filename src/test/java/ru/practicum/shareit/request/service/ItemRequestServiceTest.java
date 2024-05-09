package ru.practicum.shareit.request.service;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Nested
@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @Mock
    private UserStorage userStorage;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private ItemMapper itemMapper;

    @Test
    void addItemRequest_NotExistUserTest() {
        Long userId = 100L;
        assertThrows(ValidException.class, () -> itemRequestService.addItemRequest(ItemRequestInDto.builder().description("Test description").build(), userId));
    }

    @Test
    void getItemRequestByIdTest() {
        Long userId = 1L;
        Long requestId = 1L;
        User user = new User();
        user.setId(userId);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setRequester(user);

        ItemRequestOutDto itemRequestOutDto = new ItemRequestOutDto();
        itemRequestOutDto.setId(requestId);
        itemRequestOutDto.setRequester(user);

        when(userStorage.existsById(userId)).thenReturn(true);
        when(itemRequestStorage.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toItemRequestOutDto(itemRequest)).thenReturn(itemRequestOutDto);
        when(itemStorage.findAllByRequestId(requestId)).thenReturn(Collections.singletonList(new Item()));
        when(itemStorage.findAllByRequestId(requestId)).thenReturn(List.of(new Item()));
        itemRequestStorage.save(itemRequest);

        ItemRequestOutDto result = itemRequestService.getItemRequestById(userId, requestId);
        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals(user, result.getRequester());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void getItemRequest_NotExistUserTest() {
        Long userId = 100L;
        Long requestId = 100L;
        assertThrows(ValidException.class, () -> itemRequestService.getItemRequestById(userId, requestId));
    }

    @Test
    void getItemRequestById_NotExistItemRequestTest() {
        Long requestId = 100L;
        Long userId = 1L;
        when(userStorage.existsById(userId)).thenReturn(true);
        when(itemRequestStorage.findById(requestId)).thenReturn(Optional.empty());
        assertThrows(ValidException.class, () -> itemRequestService.getItemRequestById(userId, requestId));
    }

    @Test
    void getAllMineRequests() {
        User user = new User();
        user.setId(1L);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        ItemRequestOutDto itemRequestOutDto = new ItemRequestOutDto();
        itemRequestOutDto.setId(1L);
        itemRequestOutDto.setRequester(user);
        Item item = new Item();
        item.setId(1L);
        item.setRequestId(1L);
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(1L);
        itemResponseDto.setRequestId(1L);

        when(userStorage.existsById(user.getId())).thenReturn(true);
        when(itemRequestStorage.findAllByRequesterId(user.getId())).thenReturn(List.of(itemRequest));
        when(itemStorage.findAllByRequestIdIn(List.of(1L))).thenReturn(List.of(item));
        when(itemRequestMapper.toItemRequestOutDto(itemRequest)).thenReturn(itemRequestOutDto);
        when(itemMapper.toItemResponseDto(item)).thenReturn(itemResponseDto);
        List<ItemRequestOutDto> items = itemRequestService.getAllMineRequests(user.getId());
        assertNotNull(items);
        assertEquals(1, items.size());

    }

    @Test
    void getAllMineRequests_NotExistUserTest() {
        Long userId = 100L;
        assertThrows(ValidException.class, () -> itemRequestService.getAllMineRequests(userId));
    }
}