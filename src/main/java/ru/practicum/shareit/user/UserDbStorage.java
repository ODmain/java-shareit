package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Repository
public class UserDbStorage implements UserStorage {

    HashMap<Long, User> users = new HashMap<>();

    HashMap<Long, String> emails = new HashMap<>();

    private long id = 0;

    private User setUserId(User user) {
        user.setId(++id);
        return user;
    }

    @Override
    public UserDto addUser(User user) {
        User us = setUserId(user);
        users.put(user.getId(), user);
        emails.put(us.getId(), user.getEmail());
        return toUserDto(us);
    }

    @Override
    public UserDto updateUser(User user, Long userId) {
        User u = users.get(userId);
        if (user.getEmail() != null) {
            u.setEmail(user.getEmail());
            emails.remove(userId);
            emails.put(userId, u.getEmail());
        }
        if (user.getName() != null) {
            u.setName(user.getName());
        }
        return toUserDto(u);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> us = new ArrayList<>(users.values());
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : us) {
            usersDto.add(toUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto getUser(Long userId) {
        return toUserDto(users.get(userId));
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
        emails.remove(userId);
    }

    @Override
    public boolean existsUser(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean existsUserEmail(User user) {
        for (String s : emails.values()) {
            return user.getEmail().equals(s);
        }
        return false;
    }

    @Override
    public Long findEmailOwner(String email) {
        for (Long l : emails.keySet()) {
            if (emails.get(l).equals(email)) {
                return l;
            }
        }
        return null;
    }
}