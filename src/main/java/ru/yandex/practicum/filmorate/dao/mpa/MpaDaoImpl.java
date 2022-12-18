package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {

        return jdbcTemplate.query("SELECT * FROM mpa", (rs, rowNum) -> mappingMpa(rs));
    }

    @Override
    public Optional<Mpa> getById(int id) {

        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(
                "SELECT * FROM mpa WHERE id = ?", id);

        if (mpaRow.next()) {

            Mpa mpa = new Mpa(mpaRow.getInt("id"), mpaRow.getString("name"));

            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    private Mpa mappingMpa(ResultSet resultSet) throws SQLException {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
