package ru.yandex.practicum.filmorate.dao.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {

        return jdbcTemplate.query("SELECT * FROM genre", (rs, rowNum) -> mappingGenre(rs));
    }

    @Override
    public Optional<Genre> getById(int id) {

        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(
                "SELECT * FROM genre WHERE id = ?", id);

        if (genreRow.next()) {

            Genre genre = new Genre(genreRow.getInt("id"), genreRow.getString("name"));

            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    private Genre mappingGenre(ResultSet resultSet) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
