package com.hamid.usermanagement.mapper;

import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.dto.response.UserResponse;
import com.hamid.usermanagement.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .taxCode(request.getTaxCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(request.getRoles())
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .taxCode(user.getTaxCode())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }
}