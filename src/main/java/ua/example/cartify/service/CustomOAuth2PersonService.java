package ua.example.cartify.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Person;
import ua.example.cartify.security.OAuth2Person;

@Service
@RequiredArgsConstructor
public class CustomOAuth2PersonService extends DefaultOAuth2UserService {
    private final RegistrationService registrationService;
    private final PersonService personService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Person person = registrationService.findOrRegisterOauth2User(oAuth2User.getAttributes());
        // UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(oAuth2User,
        //         null, oAuth2User.getAuthorities());
        return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority(person.getRole())),
                oAuth2User.getAttributes(), "email");

    }

    public OAuth2User loadRemember(String email) {
        Person person = personService.findByEmail(email).get();
        OAuth2Person oAuth2Person = new OAuth2Person(person);
        return new DefaultOAuth2User(Collections.singletonList(
            new SimpleGrantedAuthority(person.getRole())), 
            oAuth2Person.getAttributes(), 
            "email");
    }

}
