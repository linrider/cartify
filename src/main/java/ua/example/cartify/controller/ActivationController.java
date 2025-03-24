package ua.example.cartify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.service.ActivationServise;

@Controller
@RequiredArgsConstructor
public class ActivationController {
    public final ActivationServise activationServise;

    @GetMapping("/activation/{code}")
    public String activatePerson(@PathVariable("code") String code) {
        if (activationServise.activateAccount(code)) {
            return "auth/succes_activation";
        } else {
            return "auth/fail_activation";
        }
    }
}
