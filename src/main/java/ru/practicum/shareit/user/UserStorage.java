package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    UserDto addUser(User user);

    UserDto updateUser(User userDto, Long userId);

    List<UserDto> getAllUsers();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);

    boolean existsUser(Long id);

    boolean existsUserEmail(User user);

    Long findEmailOwner(String email);
}
