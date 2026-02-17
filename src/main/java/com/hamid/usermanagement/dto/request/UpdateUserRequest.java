package com.hamid.usermanagement.dto.request;

import com.hamid.usermanagement.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Tax code is required")
    private String taxCode;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    // Nullable: se null non aggiorno i ruoli
    private Set<Role> roles;
}