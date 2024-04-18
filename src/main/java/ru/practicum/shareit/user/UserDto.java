package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static ru.practicum.shareit.constant.Constant.REGEX_EMAIL;

@Data
@Builder
public class UserDto {
    Long id;
    String name;
    @NotEmpty
    @Email(regexp = REGEX_EMAIL, message = "В 'email' использованы запрещённые символы")
    String email;
}
