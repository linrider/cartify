package ua.example.cartify.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;
import ua.example.cartify.security.PersonDetails;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService{
    private final PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personService.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with %s username not found", username));
        }

        return new PersonDetails(person.get());
    }


}
