package ru.yandex.practicum.filmorate.service.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.EntityExistException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreDao genreDao;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public Genre getById(int id) {
        return genreDao.getById(id).orElseThrow(
                () -> {throw new EntityExistException("Ошибка поиска жанра, запись с id = " + id + " не найдена.");
                }
        );
    }
}
