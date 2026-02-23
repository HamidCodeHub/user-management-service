package com.hamid.usermanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // ← AGGIUNGI QUESTA RIGA
@DisplayName("User Integration Tests")
@SuppressWarnings("deprecation")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Complete user lifecycle - Create, Read, Update, Delete")
    void completeUserLifecycle() throws Exception {

        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setUsername("integration.test");
        createRequest.setEmail("integration@example.com");
        createRequest.setTaxCode("INTGRT90A01H501Z");
        createRequest.setFirstName("Integration");
        createRequest.setLastName("Test");
        createRequest.setRoles(Set.of(Role.DEVELOPER));

        String createResponse = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username", is("integration.test")))
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("integration.test")));

        String updateJson = """
                {
                    "username": "updated.test",
                    "taxCode": "INTGRT90A01H501Z",
                    "firstName": "Updated",
                    "lastName": "Test",
                    "roles": ["OPERATOR"]
                }
                """;

        mockMvc.perform(put("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updated.test")))
                .andExpect(jsonPath("$.firstName", is("Updated")));

        mockMvc.perform(delete("/api/v1/users/" + userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isNotFound());
    }
}