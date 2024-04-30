package ru.practicum.shareit.item;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
public class ItemDto {
    @Positive
    Long id;
    @NotEmpty
    @Size(max = 100, min = 1, message = "Максимальная длина названия - 100 символов")
    String name;
    @NotEmpty
    @Size(max = 255, min = 1, message = "Максимальная длина описания - 255 символов")
    String description;
    Boolean available;
    User owner;
}
