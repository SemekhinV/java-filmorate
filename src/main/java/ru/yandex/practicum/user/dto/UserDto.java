package ru.yandex.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.user.entity.Friend;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Min(value = 0, message = "Значение поля id должно быть больше нуля")
    private int id;

    @Email(message = "Некорректный вид email-адреса.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "Поле login не должно содержать пробелов.")
    private String login;

    @NotEmpty
    private String name;

    @PastOrPresent
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;

    private List<Friend> friends;

    public void addFriend(Friend friend) {

        if (this.friends == null) {

            this.friends = new ArrayList<>();
        }

        this.friends.add(friend);
    }
}
