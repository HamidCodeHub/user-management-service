package com.hamid.usermanagement.controller;

import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.dto.request.UpdateUserRequest;
import com.hamid.usermanagement.dto.response.UserResponse;
import com.hamid.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "REST APIs for managing users and their application roles")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a complete list of all registered users with their roles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of users",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            )
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve detailed information about a specific user by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the provided ID",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create new user",
            description = "Register a new user in the system with assigned roles. Email must be unique and cannot be changed later."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (validation errors)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email or username already exists in the system",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "User creation request with all required fields", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing user",
            description = "Modify user information and/or roles. Note: Email field is immutable and cannot be changed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (validation errors)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the provided ID",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "Unique identifier of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "User update request with modified fields", required = true)
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Permanently remove a user from the system. This operation cannot be undone."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the provided ID",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Unique identifier of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}