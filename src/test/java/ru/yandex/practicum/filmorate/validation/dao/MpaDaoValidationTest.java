package ru.yandex.practicum.filmorate.validation.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDaoImpl;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MpaDaoValidationTest {

    private final MpaDaoImpl mpaDao;

    @Test
    void getMpaByIdTest() {
        assertThat(
                mpaDao.getById(1))
                .isPresent()
                .hasValueSatisfying(
                        mpa -> assertThat(mpa).hasFieldOrPropertyWithValue("name", "G"));
    }

    @Test
    void getALlMpaTest() {

        List<Mpa> mpaList = mpaDao.getAll();

        assertEquals(mpaList.get(1).getName(), "PG");
        assertEquals(5, mpaList.size());
    }
}
