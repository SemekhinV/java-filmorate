package ru.yandex.practicum.filmorate.service;

import javax.validation.Valid;
import java.util.Map;

public interface ServicePattern<T> {
    //В качестве названия методов невозможно использовать просто add, delete
    //И т.д., потому что возникает конфликт с сервисными методами
    T addData(@Valid T entity);             //Аналогично реализации хранилища,

    //Используем общий интерфейс для описания
    //Функционала обобщенного сервиса
    T getData(int id);

    T removeData(@Valid T entity);

    T updateData(@Valid T entity);

    Map<Integer, T> getAll();

}
