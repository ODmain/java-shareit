package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidException;

import java.util.List;

import static ru.practicum.shareit.user.UserMapper.fromUserDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto addUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidException("Переданы неверные данные", HttpStatus.BAD_REQUEST);
        }
        if (userStorage.existsUserEmail(fromUserDto(userDto))) {
            throw new ValidException("Пользователь с таким email уже есть", HttpStatus.CONFLICT);
        }
        return userStorage.addUser(fromUserDto(userDto));
    }

    public UserDto updateUser(UserDto userDto, Long userId) {
        if (userDto == null) {
            throw new ValidException("Переданы неверные данные", HttpStatus.BAD_REQUEST);
        }
        if (!userStorage.existsUser(userId)) {
            throw new ValidException("Пользователя с таким id не существует", HttpStatus.NOT_FOUND);
        }
        if (userDto.getEmail() != null) {
            if (userStorage.findEmailOwner(userDto.getEmail()) != null) {
                if (!userStorage.findEmailOwner(userDto.getEmail()).equals(userId)) {
                    throw new ValidException("Пользователь с таким email уже есть", HttpStatus.CONFLICT);
                }
            }
        }
        return userStorage.updateUser(fromUserDto(userDto), userId);
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }


    public UserDto getUser(Long userId) {
        if (!userStorage.existsUser(userId)) {
            throw new ValidException("Пользователя с таким id не существует", HttpStatus.NOT_FOUND);
        }
        return userStorage.getUser(userId);
    }


    public void deleteUser(Long userId) {
        if (!userStorage.existsUser(userId)) {
            throw new ValidException("Пользователя с таким id не существует", HttpStatus.NOT_FOUND);
        }
        userStorage.deleteUser(userId);
    }
}

