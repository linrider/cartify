package ua.example.cartify.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;
import ua.example.cartify.security.PersonDetails;
import ua.example.cartify.service.PersonService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PersonService personService;

    @GetMapping
    public String getMainPage(@AuthenticationPrincipal PersonDetails personDetails,
            @AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (personDetails != null) {
            Person person = personDetails.person();
            model.addAttribute("person", person);
        } else if (oauth2User != null) {
            String email = (String) oauth2User.getAttributes().get("email");
            Person person = personService.findByEmail(email).orElseThrow();
            model.addAttribute("person", person);
        }
        return "main";
    }
}
