package ua.example.cartify.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ActivationServise activationServise;
    private final EmailService emailService;
    private final PersonService personService;

    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setEnable(false);
        personService.save(person);
        String key = activationServise.saveActivation(person.getEmail());
        emailService.sendActivationEmail(person.getEmail(), key);
    }

    public Person findOrRegisterOauth2User(Map<String, Object> atributs) {
        String email = (String) atributs.get("email");
        Optional<Person> person = personService.findByEmail(email);

        if (person.isPresent()) {
            return person.get();
        }

        Person person2 = new Person();
        person2.setEmail(email);
        person2.setUsername(email);
        person2.setRole("ROLE_USER");
        person2.setEnable(true);
        person2.setProvider("google");
        person2.setProviderId((String) atributs.get("sub"));
        String password = generatePassword();
        person2.setPassword(passwordEncoder.encode(password));

        emailService.sendWelcomeEmail(email, password);

        jdbcTemplate.update("INSERT INTO person (username, password, email, role, enable, provider, provider_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                person2.getUsername(), person2.getPassword(), person2.getEmail(), person2.getRole(),
                person2.isEnable(), person2.getProvider(), person2.getProviderId());
        return personService.findByEmail(email).get();
    }

    private String generatePassword() {
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789*/.%";
        for (int i = 0; i < 14; i++) {
            builder.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }
        return builder.toString();
    }
}
