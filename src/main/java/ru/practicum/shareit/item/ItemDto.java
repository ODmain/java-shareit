package ru.practicum.shareit.item;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class ItemDto {
    Long id;
    @NotEmpty
    String name;
    @NotEmpty
    String description;
    Boolean available;
    User owner;
}
