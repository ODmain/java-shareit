package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import static ru.practicum.shareit.constant.Constant.REGEX_EMAIL;

@Data
@Builder
public class UserDto {
    @Positive
    Long id;
    @Size(max = 30, min = 1, message = "Максимальная длина имени - 30 символов")
    String name;
    @NotEmpty
    @Email(regexp = REGEX_EMAIL, message = "В 'email' использованы запрещённые символы")
    String email;
}
