package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.EntityExistException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaDao mpaDao;

    @Override
    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }

    @Override
    public Mpa getById(int id) {
        return mpaDao.getById(id).orElseThrow(
                () -> {throw new EntityExistException("Ошибка поиска MPA, запись с id = " + id + " не найдена.");}
        );
    }
}
