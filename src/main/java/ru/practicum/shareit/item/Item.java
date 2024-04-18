package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

@Data
@Builder
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    User owner;
}
