package com.hamid.usermanagement.mapper;


import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.dto.response.UserResponse;
import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Mapper Tests")
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    @DisplayName("Should map CreateUserRequest to User entity")
    void toEntity_ShouldMapCorrectly() {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test.user");
        request.setEmail("test@example.com");
        request.setTaxCode("TSTUSER90A01H501Z");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRoles(Set.of(Role.DEVELOPER, Role.OPERATOR));

        User user = userMapper.toEntity(request);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("test.user");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getTaxCode()).isEqualTo("TSTUSER90A01H501Z");
        assertThat(user.getFirstName()).isEqualTo("Test");
        assertThat(user.getLastName()).isEqualTo("User");
        assertThat(user.getRoles()).containsExactlyInAnyOrder(Role.DEVELOPER, Role.OPERATOR);
    }

    @Test
    @DisplayName("Should map User entity to UserResponse")
    void toResponse_ShouldMapCorrectly() {

        User user = User.builder()
                .id(1L)
                .username("test.user")
                .email("test@example.com")
                .taxCode("TSTUSER90A01H501Z")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(Role.DEVELOPER))
                .build();

        UserResponse response = userMapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("test.user");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getTaxCode()).isEqualTo("TSTUSER90A01H501Z");
        assertThat(response.getFirstName()).isEqualTo("Test");
        assertThat(response.getLastName()).isEqualTo("User");
        assertThat(response.getRoles()).contains(Role.DEVELOPER);
    }
}