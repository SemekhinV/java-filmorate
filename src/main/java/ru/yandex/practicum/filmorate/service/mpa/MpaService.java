package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaService {

    List<Mpa> getAll();

    Mpa getById(int id);
}
