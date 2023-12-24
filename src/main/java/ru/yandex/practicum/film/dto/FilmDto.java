package ru.yandex.practicum.film.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.film.entity.Like;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {

    Long id;

    @Positive(message = "Продолжительность должна быть положительной.")
    int duration;

    @NotBlank(message = "Имя фильма не может быть пустым.")
    String name;

    @Length(min = 1, max = 200, message = "Длинна описания не может быть меньше 1 и больше 200 символов.")
    String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;

    List<Like> likes;

    String genre;

    String MPA;

    public void addLike(Like like) {

        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }

        this.likes.add(like);
    }

    public void addLike(List<Like> like) {

        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }

        this.likes.addAll(like);
    }
}
