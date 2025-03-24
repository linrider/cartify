package ua.example.cartify.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;
import ua.example.cartify.security.PersonDetails;
import ua.example.cartify.service.PersonService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final PersonService personService;

    @GetMapping()
    public String adminPage(@AuthenticationPrincipal PersonDetails personDetails,
            @AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (personDetails != null) {
            Person person = personDetails.person();
            if(person.getRole().equals("ROLE_ROOTADMIN")) {
                model.addAttribute("rootadmin", person);
            } else {
                model.addAttribute("admin", person);
            }
        } else if (oauth2User != null) {
            String email = (String) oauth2User.getAttributes().get("email");
            Person person = personService.findByEmail(email).orElseThrow();
            if(person.getRole().equals("ROLE_ROOTADMIN")) {
                model.addAttribute("rootadmin", person);
            } else {
                model.addAttribute("admin", person);
            }
        }
        model.addAttribute("users", personService.getAllUsers());
        model.addAttribute("admins", personService.getAllAdmins());
        return "admin/administration";
    }

    @PutMapping("/make-admin")
    public String makeAdmin(@RequestParam("id") int id) {
        personService.setAdminRole(id);
        return "redirect:/admin";
    }

    @PutMapping("/return-user")
    public String returnUser(@RequestParam("id") int id) {
        personService.returnUserRole(id);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        personService.deleteById(id);
        return "redirect:/admin";
    }

}
