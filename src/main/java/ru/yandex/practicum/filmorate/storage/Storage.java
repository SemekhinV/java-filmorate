package ru.yandex.practicum.filmorate.storage;

import javax.validation.Valid;
import java.util.Map;

public interface Storage<T> {
    //Удобнее иметь один интерфейс под хранилище, чтобы не
    T addEntity(@Valid T entity);    //Захламлять код лишними классами, т.к.

    //При обе реализации имеют одинаковые методы
    T getEntity(int id);

    T removeEntity(@Valid T entity);

    Map<Integer, T> getAll();

    T updateEntity(@Valid T entity);
}
