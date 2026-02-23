package com.hamid.usermanagement.util;


import com.hamid.usermanagement.dto.response.UserResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class UserResponseFilter {

    private static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
    private static final GrantedAuthority ROLE_OPERATOR = new SimpleGrantedAuthority("ROLE_OPERATOR");

    public UserResponse applyFiltering(UserResponse response, Collection<? extends GrantedAuthority> authorities) {
        if (response == null) {
            return null;
        }

        if (hasRole(authorities, ROLE_ADMIN)) {
            return response;
        }

        if (hasRole(authorities, ROLE_OPERATOR)) {
            return UserResponse.builder()
                    .id(response.getId())
                    .username(response.getUsername())
                    .email(response.getEmail())
                    .taxCode(null)  // ← Hidden for OPERATOR
                    .firstName(response.getFirstName())
                    .lastName(response.getLastName())
                    .roles(response.getRoles())
                    .build();
        }

        return UserResponse.builder()
                .id(response.getId())
                .username(response.getUsername())
                .email(response.getEmail())
                .taxCode(null)  // ← Hidden for USER
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .roles(null)  // ← Hidden for USER
                .build();
    }

    public List<UserResponse> applyFiltering(
            List<UserResponse> responses,
            Collection<? extends GrantedAuthority> authorities) {

        if (responses == null) {
            return null;
        }

        return responses.stream()
                .map(response -> applyFiltering(response, authorities))
                .toList();
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, GrantedAuthority role) {
        if (authorities == null) {
            return false;
        }
        return authorities.contains(role);
    }
}
