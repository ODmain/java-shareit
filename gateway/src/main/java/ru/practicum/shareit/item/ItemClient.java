package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(Long userId, ItemRequestDto itemRequestDto) {
        String url = "";
        return post(url, userId, itemRequestDto);
    }

    public ResponseEntity<Object> updateItem(ItemRequestDto itemRequestDto, Long userId, Long itemId) {
        String url = "/" + itemId;
        return patch(url, userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        String url = "/" + itemId;
        return get(url, userId);
    }

    public ResponseEntity<Object> getItemsOfOwner(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItem(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        String url = "/" + itemId + "/comment";
        return post(url, userId, commentRequestDto);
    }
}
