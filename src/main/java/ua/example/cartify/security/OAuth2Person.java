package ua.example.cartify.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import ua.example.cartify.model.Person;

@AllArgsConstructor
public class OAuth2Person implements OAuth2User{
    private Person person;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
            "id", person.getId(),
            "username", person.getUsername(),
            "email", person.getEmail(),
            "role", person.getRole(),
            "enable", person.isEnable(),
            "provider", person.getProvider(),
            "providerId", person.getProviderId() 
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getName() {
        return person.getUsername();
    }

}
