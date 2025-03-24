package ua.example.cartify.security;

import java.io.IOException;
import java.util.Base64;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ua.example.cartify.service.CustomOAuth2PersonService;

@Component
@RequiredArgsConstructor
public class OAuth2RememberMeFilter extends OncePerRequestFilter {
    private static final String COOKIE_NAME = "OAUTH2_REMEMBER_ME";
    private final CustomOAuth2PersonService customOAuth2PersonService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals(COOKIE_NAME)) {
                                String tokenValue = new String(Base64.getDecoder().decode(cookie.getValue()));
                                OAuth2User oAuth2User = customOAuth2PersonService.loadRemember(tokenValue);
                                Authentication auth = new OAuth2AuthenticationToken(
                                    oAuth2User, 
                                    oAuth2User.getAuthorities(), 
                                    "oauth2");
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                            }
                        }
                    }
                }
                filterChain.doFilter(request, response);
    }

}
