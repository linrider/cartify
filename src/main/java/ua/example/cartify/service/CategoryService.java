package ua.example.cartify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Category;

@Service
@RequiredArgsConstructor
public class CategoryService implements CrudService<Integer, Category> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getAll() {
        return jdbcTemplate.query("SELECT * FROM category", new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Category getById(Integer id) {
        return jdbcTemplate
                .queryForObject(
                        "SELECT * FROM category WHERE id=?",
                        new BeanPropertyRowMapper<>(Category.class),
                        id);
    }

    @Override
    public void save(Category category) {
        jdbcTemplate.update(
                "INSERT INTO category (name, description) " +
                        "VALUES (?, ?)",
                category.getName(), category.getDescription());
    }

    @Override
    public void edit(Integer id, Category category) {
        jdbcTemplate.update(
                "UPDATE category SET name=?, description=? WHERE id=?",
                category.getName(), category.getDescription());
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM category WHERE id=?", id);
    }

    public Optional<Category> getByName(String name) {
        try {
            Category category = jdbcTemplate.queryForObject(
                    "SELECT * FROM category WHERE name=?",
                    new BeanPropertyRowMapper<>(Category.class),
                    name);
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
