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
public class PersonService implements CrudService<Integer, Person> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Person getById(Integer id) {
        return jdbcTemplate
                .queryForObject(
                        "SELECT * FROM person WHERE id=?",
                        new BeanPropertyRowMapper<>(Person.class),
                        id);
    }

    @Override
    public List<Person> getAll() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    @Override
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person (username, password, email, role, enable) " +
                "VALUES (?, ?, ?, ?, ?)",
                person.getUsername(), person.getPassword(), person.getEmail(), person.getRole(),
                person.isEnable());
    }

    @Override
    public void edit(Integer id, Person person) {
        jdbcTemplate.update("UPDATE person SET username=? WHERE id=?", person.getUsername(), person.getId());
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
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

    public List<Person> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM person WHERE role='ROLE_USER'",
                new BeanPropertyRowMapper<>(Person.class));
    }

    public List<Person> getAllAdmins() {
        return jdbcTemplate.query("SELECT * FROM person WHERE role='ROLE_ADMIN'",
                new BeanPropertyRowMapper<>(Person.class));
    }

    public void setAdminRole(int id) {
        jdbcTemplate.update("UPDATE person SET role='ROLE_ADMIN' WHERE id=?", id);
    }

    public void returnUserRole(int id) {
        jdbcTemplate.update("UPDATE person SET role='ROLE_USER' WHERE id=?", id);
    }

}
