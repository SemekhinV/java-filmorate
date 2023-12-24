package ru.yandex.practicum.film.mapepr;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.film.dto.FilmDto;
import ru.yandex.practicum.film.entity.Film;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface FilmMapper {

    FilmDto toDto(Film film);

    Film toEntity(FilmDto dto);

    List<FilmDto> toDtoList(List<Film> films);
}
