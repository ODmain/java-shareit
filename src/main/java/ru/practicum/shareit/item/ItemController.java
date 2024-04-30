package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestBody @Valid ItemRequestDto itemRequestDto) {
        itemRequestDto.setOwnerId(userId);
        return itemService.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestBody ItemRequestDto itemRequestDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long itemId) {
        return itemService.updateItem(itemRequestDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingsDto getItem(@PathVariable Long itemId,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemWithBookingsDto> getItemsOfOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsOfOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchForTheItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody @Valid CommentRequestDto commentRequestDto) {
        return itemService.addComment(userId, itemId, commentRequestDto);
    }
}
