package ua.example.cartify.service;

import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;

@Service
@RequiredArgsConstructor
public class ActivationServise {
    private final JdbcTemplate jdbcTemplate;

 public String saveActivation(String email) {
        String code = generateCode();
        jdbcTemplate.update("INSERT INTO activation (person_email, key) VALUES (?, ?)", email, code);
        return code;
    } 

    public boolean activateAccount(String key) {
        Optional<Person> person = jdbcTemplate.query("SELECT person.* FROM person JOIN activation ON " + 
        "person.email = activation.person_email WHERE activation.key=?", new BeanPropertyRowMapper<>(Person.class), key)
        .stream()
        .findAny();
        if (person.isPresent()) {
            jdbcTemplate.update("UPDATE person SET enable=true WHERE person.id=?", person.get().getId());
            jdbcTemplate.update("DELETE FROM activation WHERE key=?", key);
            return true;
        }
        return false;
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 14; i++) {
            builder.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }
        return builder.toString();
    }    
}
