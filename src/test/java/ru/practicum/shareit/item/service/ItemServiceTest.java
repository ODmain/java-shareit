package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserStorage userStorage;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private CommentStorage commentStorage;

    @Mock
    private CommentMapper commentMapper;

    @Test
    void addItemTest() {
        Long userId = 1L;
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("item1");
        item.setDescription("description1");
        ItemResponseDto responseDto = new ItemResponseDto();
        responseDto.setName("item1");
        responseDto.setDescription("description1");
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item1");
        itemRequestDto.setDescription("description1");
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        when(userStorage.existsById(userId)).thenReturn(true);
        when(itemMapper.toItemFromRequest(itemRequestDto)).thenReturn(item);
        when(itemStorage.save(item)).thenReturn(item);
        when(itemMapper.toItemResponseDto(item)).thenReturn(responseDto);
        ItemResponseDto result = itemService.addItem(userId, itemRequestDto);
        assertNotNull(result);
        assertEquals("item1", result.getName());
        assertEquals("description1", result.getDescription());
    }

    @Test
    void addItem_NotExistUserTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item1");
        itemRequestDto.setDescription("description1");
        Long itemId = 100L;
        assertThrows(ValidException.class, () -> itemService.addItem(itemId, itemRequestDto));
    }

    @Test
    void updateItemTest() {
        ItemResponseDto responseDto = new ItemResponseDto();
        responseDto.setName("Updated Item Name");
        responseDto.setDescription("Updated Item Description");
        responseDto.setAvailable(true);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Updated Item Name");
        itemRequestDto.setDescription("Updated Item Description");
        itemRequestDto.setAvailable(true);
        Long userId = 1L;
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("Initial Item Name");
        item.setDescription("Initial Item Description");
        item.setAvailable(false);
        User owner = new User();
        owner.setId(userId);
        item.setOwner(owner);

        Mockito.when(itemStorage.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(itemMapper.toItemFromRequest(itemRequestDto)).thenReturn(item);
        Mockito.when(itemMapper.toItemResponseDto(item)).thenReturn(responseDto);
        when(itemStorage.save(item)).thenReturn(item);
        ItemResponseDto updatedItemResponseDto = itemService.updateItem(itemRequestDto, userId, itemId);
        assertEquals("Updated Item Name", updatedItemResponseDto.getName());
        assertEquals("Updated Item Description", updatedItemResponseDto.getDescription());
        assertTrue(updatedItemResponseDto.getAvailable());
    }

    @Test
    void updateItem_ItemRequestNullTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        assertNull(itemRequestDto.getDescription());
        assertThrows(ValidException.class, () -> itemService.updateItem(itemRequestDto, 1L, 1L));
    }

    @Test
    void updateItem_NotExistItemTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Updated Item Name");
        itemRequestDto.setDescription("Updated Item Description");
        itemRequestDto.setAvailable(true);
        Long userId = 100L;
        Long itemId = 100L;
        assertThrows(ValidException.class, () -> itemService.updateItem(itemRequestDto, itemId, userId));
    }

//    @Test
//    void getItem() {
//        Long itemId = 1L;
//        Long userId = 1L;
//        Item item = new Item();
//        item.setId(itemId);
//        User user = new User();
//        user.setId(userId);
//        item.setOwner(user);
//        item.setName("Test Item");
//        Booking booking1 = new Booking();
//        booking1.setStart(LocalDateTime.now().minusDays(1));
//        Booking booking2 = new Booking();
//        booking2.setStart(LocalDateTime.now().plusDays(1));
//        List<Booking> bookings = new ArrayList<>();
//        bookings.add(booking1);
//        bookings.add(booking2);
//        CommentResponseDto comment1 = new CommentResponseDto();
//        comment1.setItemId(itemId);
//        CommentResponseDto comment2 = new CommentResponseDto();
//        comment2.setItemId(itemId);
//        List<CommentResponseDto> comments = new ArrayList<>();
//        comments.add(comment1);
//        comments.add(comment2);
//        Comment commentt = new Comment();
//        comment1.setItemId(itemId);
//        Comment commenttt = new Comment();
//        comment2.setItemId(itemId);
//        List<Comment> commentss = new ArrayList<>();
//        commentss.add(commentt);
//        commentss.add(commenttt);
//
//        BookingShortDto lastBooking = new BookingShortDto();
//        lastBooking.setId(1L);
//        BookingShortDto nextBooking = new BookingShortDto();
//        nextBooking.setId(2L);
//        ItemWithBookingsDto responseDto = new ItemWithBookingsDto();
//        responseDto.setName(item.getName());
//        responseDto.setLastBooking(lastBooking);
//        responseDto.setNextBooking(nextBooking);
//        responseDto.setComments(comments);
//
//        when(itemStorage.findById(itemId)).thenReturn(Optional.of(item));
//        when(bookingStorage.findAllByItem_IdAndStatusIsNot(itemId, Status.REJECTED)).thenReturn(bookings);
//        when(commentStorage.findAllByItem_IdOrderByCreatedDesc(itemId)).thenReturn(commentss);
//        itemStorage.save(item);
//        ItemWithBookingsDto result = itemService.getItem(itemId, userId);
//        result.setName(item.getName());
//        result.setComments(comments);
//        result.setNextBooking(nextBooking);
//        result.setLastBooking(lastBooking);
//
//
//        assertEquals(item.getName(), result.getName());
//        assertEquals(comments.size(), result.getComments().size());
//        assertNotNull(result.getLastBooking());
//        assertNotNull(result.getNextBooking());
//
//
//    }

//    @Test
//    void getItemsOfOwner() {
//        List<ItemWithBookingsDto> responseItems = itemMapper.toItemWithBookingsListDto(itemStorage.findAllByOwnerIdOrderById(userId));
//        List<Long> itemsIds = responseItems.stream()
//                .map(ItemWithBookingsDto::getId)
//                .collect(Collectors.toList());
//        Map<Long, List<BookingResponseDto>> bookings = bookingStorage.findAllByItem_IdInAndStatusIsNot(itemsIds, REJECTED).stream()
//                .map(bookingMapper::toBookingResponseDto)
//                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
//        Map<Long, List<CommentResponseDto>> comments = commentStorage.findAllByItem_IdInOrderByCreatedDesc(itemsIds).stream()
//                .map(commentMapper::toCommentResponseDto)
//                .collect(Collectors.groupingBy(CommentResponseDto::getItemId));
//        LocalDateTime now = LocalDateTime.now();
//
//        return responseItems.stream().map(itemDto -> {
//            Long itemId = itemDto.getId();
//            return getItemWithBookingsAndCommentsDto(itemDto, comments.get(itemId), bookings.get(itemId), now);
//        }).collect(Collectors.toList());
//
//
//    }
//
//
//}

//    @Test
//    void searchItem() {
//        int from = 0;
//        int size = 10;
//        Pageable pageable = Paginator.getPageable(from, size);
//        String text = "text";
//        List<Item> itemList = new ArrayList<>();
//        itemList.add(new Item());
//        itemList.add(new Item());
//        itemList.add(new Item());
//        Page<Item> itemPage = new PageImpl<>(itemList, PageRequest.of(0, 10), itemList.size());
//
//        when(itemMapper.toItemResponseDto(Mockito.any(Item.class))).thenReturn(new ItemDto());
//        when(itemRepository.searchByNameAndDescriptionAndAvailable(text, pageable)).thenReturn(itemPage);
//
//        List<ItemDto> response = itemService.searchAvailableItem(text, from, size);
//        assertEquals(itemList.size(), response.size());
//
//        when(itemMapper.toItemResponseListDto(List.of(item))).thenReturn(List.of(itemResponseDto));
//        List<Item> result = itemStorage.searchItem("vvv");
//        assertEquals(result.size(), 1);
//    }

//    @Test
//    void addComment() {
//        if (!userStorage.existsById(userId)) {
//            throw new ValidException("Пользователя с таким id не существует", HttpStatus.NOT_FOUND);
//        }
//        if (!itemStorage.existsById(itemId)) {
//            throw new ValidException("Предмета с таким id не существует", HttpStatus.NOT_FOUND);
//        }
//        LocalDateTime now = LocalDateTime.now();
//
//        boolean isBookingConfirmed = bookingStorage.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(itemId, userId, APPROVED, now);
//        if (!isBookingConfirmed) {
//            throw new ValidException("Пользователь не брал в аренду этот предмет", HttpStatus.BAD_REQUEST);
//        }
//        UserDto userDto = userMapper.toUserDto(userStorage.findById(userId).orElseThrow(() -> new ValidException("Пользователя с таким id не существует", HttpStatus.NOT_FOUND)));
//        commentRequestDto.setCreated(now);
//        commentRequestDto.setAuthorId(userId);
//        commentRequestDto.setItemId(itemId);
//
//        CommentResponseDto commentResponseDto = commentMapper.toCommentResponseDto(commentStorage.save(commentMapper.toCommentFromRequest(commentRequestDto)));
//        commentResponseDto.setAuthorName(userDto.getName());
//
//        return commentResponseDto;
//    }
}
