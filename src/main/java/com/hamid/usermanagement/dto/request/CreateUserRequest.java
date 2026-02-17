package com.hamid.usermanagement.dto.request;

import com.hamid.usermanagement.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Tax code is required")
    private String taxCode;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "At least one role is required")
    private Set<Role> roles;
}