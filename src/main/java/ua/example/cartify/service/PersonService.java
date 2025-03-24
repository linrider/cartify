package ua.example.cartify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final JdbcTemplate jdbcTemplate;

    public Person getById(int id) {
        return jdbcTemplate
                .queryForObject(
                        "SELECT * FROM person WHERE id=?",
                        new BeanPropertyRowMapper<>(Person.class),
                        id);
    }

    public Optional<Person> findByEmail(String email) {
        try {
        Person person = jdbcTemplate.queryForObject("select * from person where email=?", 
                new BeanPropertyRowMapper<>(Person.class),
                email);
                return Optional.ofNullable(person);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Person> findByUsername(String username) {
        return jdbcTemplate.query("select * from person where username=?", 
                new BeanPropertyRowMapper<>(Person.class), username)
                .stream()
                .findAny();
    }

    public List<Person> getAll() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }
    
    public List<Person> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM person WHERE role='ROLE_USER'", new BeanPropertyRowMapper<>(Person.class));
    }
    
    public List<Person> getAllAdmins() {
        return jdbcTemplate.query("SELECT * FROM person WHERE role='ROLE_ADMIN'", new BeanPropertyRowMapper<>(Person.class));
    }

    public void setAdminRole(int id) {
        jdbcTemplate.update("UPDATE person SET role='ROLE_ADMIN' WHERE id=?", id);
    }

    public void returnUserRole(int id) {
        jdbcTemplate.update("UPDATE person SET role='ROLE_USER' WHERE id=?", id);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }
}
