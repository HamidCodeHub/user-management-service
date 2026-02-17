package com.hamid.usermanagement.dto.response;

import com.hamid.usermanagement.entity.Role;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String taxCode;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
}