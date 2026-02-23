package com.hamid.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.dto.request.UpdateUserRequest;
import com.hamid.usermanagement.dto.response.UserResponse;
import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)  // ← AGGIUNGI QUESTA RIGA (disabilita Security nei test)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserResponse userResponse;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        userResponse = UserResponse.builder()
                .id(1L)
                .username("test.user")
                .email("test@example.com")
                .taxCode("TSTUSER90A01H501Z")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(Role.DEVELOPER))
                .build();

        createRequest = new CreateUserRequest();
        createRequest.setUsername("test.user");
        createRequest.setEmail("test@example.com");
        createRequest.setTaxCode("TSTUSER90A01H501Z");
        createRequest.setFirstName("Test");
        createRequest.setLastName("User");
        createRequest.setRoles(Set.of(Role.DEVELOPER));

        updateRequest = new UpdateUserRequest();
        updateRequest.setUsername("updated.user");
        updateRequest.setTaxCode("TSTUSER90A01H501Z");
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setRoles(Set.of(Role.OPERATOR));
    }

    @Test
    @DisplayName("GET /api/v1/users - Should return all users")
    void getAllUsers_ShouldReturnUserList() throws Exception {

        List<UserResponse> users = Arrays.asList(userResponse);
        when(userService.getAllUsers()).thenReturn(users);


        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("test.user")))
                .andExpect(jsonPath("$[0].email", is("test@example.com")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - Should return user by id")
    void getUserById_ShouldReturnUser() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(userResponse);


        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("test.user")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("POST /api/v1/users - Should create user successfully")
    void createUser_ShouldReturnCreatedUser() throws Exception {

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("test.user")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/users - Should fail with invalid email")
    void createUser_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {

        createRequest.setEmail("invalid-email");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("POST /api/v1/users - Should fail with blank username")
    void createUser_WithBlankUsername_ShouldReturnBadRequest() throws Exception {

        createRequest.setUsername("");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} - Should update user successfully")
    void updateUser_ShouldReturnUpdatedUser() throws Exception {

        UserResponse updatedResponse = UserResponse.builder()
                .id(1L)
                .username("updated.user")
                .email("test@example.com")
                .taxCode("TSTUSER90A01H501Z")
                .firstName("Updated")
                .lastName("User")
                .roles(Set.of(Role.OPERATOR))
                .build();

        when(userService.updateUser(eq(1L), any(UpdateUserRequest.class))).thenReturn(updatedResponse);


        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updated.user")))
                .andExpect(jsonPath("$.firstName", is("Updated")));

        verify(userService, times(1)).updateUser(eq(1L), any(UpdateUserRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - Should delete user successfully")
    void deleteUser_ShouldReturnNoContent() throws Exception {

        doNothing().when(userService).deleteUser(1L);


        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}