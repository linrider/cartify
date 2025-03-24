package ua.example.cartify.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;
import ua.example.cartify.service.PersonService;

@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {
    private final PersonService personService;

    @SuppressWarnings("null")
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @SuppressWarnings("null")
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personService.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already used");
        }

        if (personService.findByUsername(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "This username is already used");
        }
    }

}
