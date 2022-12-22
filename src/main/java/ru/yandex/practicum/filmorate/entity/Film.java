package ru.yandex.practicum.filmorate.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Film implements Comparable<Film> {                             //Дата выхода

    @Min(
            value = 0,
            message = "Id должен быть больше, либо равен нулю."
    )
    int id;

    @Positive(message = "Продлжительность должна быть положительной.")
    int duration;

    @NotBlank(message = "Имя фильма не может быть пустым.")
    String name;

    @Length(
            min = 1,
            max = 200,
            message = "Длинна описания не может быть меньше 1 и больше 200 символов."
    )
    String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;

    final Set<Integer> likes = new HashSet<>();

    @Override
    public int compareTo(Film film) {               //Вызов метода пропускался и сортировка не выполнялась
        return film.getLikes().size() - this.getLikes().size();
    }

    final List<Genre> genres = new ArrayList<>();

    @NotNull(message = "Ошибка создания сущности, список категорий МРА-рейтинга пуст")
    Mpa mpa;
}
