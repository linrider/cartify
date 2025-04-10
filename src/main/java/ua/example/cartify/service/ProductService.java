package ua.example.cartify.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Product;

@Service
@RequiredArgsConstructor
public class ProductService implements CrudService<Integer, Product> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Product getById(Integer id) {
        return jdbcTemplate
                .queryForObject(
                        "SELECT * FROM product WHERE id=?",
                        new BeanPropertyRowMapper<>(Product.class),
                        id);
    }

    @Override
    public List<Product> getAll() {
        return jdbcTemplate.query("SELECT * FROM product", new BeanPropertyRowMapper<>(Product.class));
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update(
                "INSERT INTO product (name, description, article, price, quantity, category_id, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                product.getName(), product.getDescription(), product.getArticle(), product.getPrice(),
                product.getQuantity(),
                product.getCategoryId(), Timestamp.valueOf(product.getCreatedAt()));
    }

    @Override
    public void edit(Integer id, Product product) {
        jdbcTemplate.update(
                "UPDATE person SET name=?, description=?, article=?, price=?, quantity=?, category_id=?, created_at=? WHERE id=?",
                product.getName(), product.getDescription(), product.getArticle(), product.getPrice(),
                product.getQuantity(),
                product.getCategoryId(), Timestamp.valueOf(product.getCreatedAt()));
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM product WHERE id=?", id);
    }

    public Optional<Product> getByArticle(String article) {
        try {
            Product product = jdbcTemplate.queryForObject(
                    "SELECT * FROM product WHERE article=?",
                    new BeanPropertyRowMapper<>(Product.class),
                    article);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
