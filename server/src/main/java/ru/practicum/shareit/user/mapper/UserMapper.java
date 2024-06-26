package ru.practicum.shareit.user.mapper;


import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User fromUserDto(UserDto userDto);

    List<UserDto> toUserDtoList(List<User> users);
}
