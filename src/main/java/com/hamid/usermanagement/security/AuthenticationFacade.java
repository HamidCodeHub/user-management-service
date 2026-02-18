package com.hamid.usermanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymous";
    }


    public Optional<Jwt> getCurrentJwt() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return Optional.of((Jwt) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public Optional<String> getCurrentUserEmail() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsString("email"));
    }

    public Optional<String> getCurrentUserSubject() {
        return getCurrentJwt()
                .map(Jwt::getSubject);
    }

    public boolean hasRole(String role) {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}