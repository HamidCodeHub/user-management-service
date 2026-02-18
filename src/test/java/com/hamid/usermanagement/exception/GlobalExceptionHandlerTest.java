package com.hamid.usermanagement.exception;


import com.hamid.usermanagement.controller.UserController;
import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Global Exception Handler Tests")
@SuppressWarnings("deprecation")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should handle UserNotFoundException with 404 status")
    void handleUserNotFoundException_ShouldReturn404() throws Exception {

        Long nonExistentId = 999L;
        when(userService.getUserById(nonExistentId))
                .thenThrow(new UserNotFoundException(nonExistentId));


        mockMvc.perform(get("/api/v1/users/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", containsString("User not found with id: 999")));

        verify(userService, times(1)).getUserById(nonExistentId);
    }

    @Test
    @DisplayName("Should handle EmailAlreadyExistsException with 409 status")
    void handleEmailAlreadyExistsException_ShouldReturn409() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test.user");
        request.setEmail("existing@example.com");
        request.setTaxCode("TSTUSER90A01H501Z");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRoles(Set.of(Role.DEVELOPER));

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("existing@example.com"));


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", containsString("Email already in use: existing@example.com")));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with 400 status")
    void handleIllegalArgumentException_ShouldReturn400() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("duplicate.user");
        request.setEmail("test@example.com");
        request.setTaxCode("TSTUSER90A01H501Z");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRoles(Set.of(Role.DEVELOPER));

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new IllegalArgumentException("Username already in use: duplicate.user"));


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", containsString("Username already in use")));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName("Should handle validation errors with 400 status")
    void handleValidationException_ShouldReturn400() throws Exception {

        CreateUserRequest invalidRequest = new CreateUserRequest();
        invalidRequest.setUsername("");
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setTaxCode("");
        invalidRequest.setFirstName("");
        invalidRequest.setLastName("");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.taxCode").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Should handle validation error - blank username only")
    void handleValidationError_BlankUsername_ShouldReturn400() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("");
        request.setEmail("valid@example.com");
        request.setTaxCode("TSTUSER90A01H501Z");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRoles(Set.of(Role.DEVELOPER));


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username", containsString("required")));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Should handle validation error - invalid email format")
    void handleValidationError_InvalidEmail_ShouldReturn400() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test.user");
        request.setEmail("not-an-email");
        request.setTaxCode("TSTUSER90A01H501Z");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRoles(Set.of(Role.DEVELOPER));


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", containsString("email")));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Should handle validation error - empty roles")
    void handleValidationError_EmptyRoles_ShouldReturn400() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test.user");
        request.setEmail("test@example.com");
        request.setTaxCode("TSTUSER90A01H501Z");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRoles(Set.of());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.roles", containsString("role")));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Should handle update validation errors")
    void handleUpdateValidationErrors_ShouldReturn400() throws Exception {

        String invalidUpdateJson = """
                {
                    "username": "",
                    "taxCode": "",
                    "firstName": "",
                    "lastName": ""
                }
                """;

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.taxCode").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());

        verify(userService, never()).updateUser(any(), any());
    }
}