package com.hamid.usermanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


import io.swagger.v3.oas.models.info.License;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter your JWT token obtained from Keycloak");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("User Management Service API")
                        .description("""
                                RESTful API for user management with JWT authentication and RBAC (Role-Based Access Control).
                                
                                ## Authentication
                                This API uses JWT tokens from Keycloak for authentication.
                                
                                **To authenticate:**
                                1. Obtain a JWT token from Keycloak (see Authentication Guide)
                                2. Click the "Authorize" button (lock icon) above
                                3. Paste your token in the format: `Bearer <your_token>`
                                4. Click "Authorize" and "Close"
                                
                                ## Authorization (RBAC)
                                Different users have different permissions:
                                
                                | User | Role | Permissions |
                                |------|------|-------------|
                                | admin_user | ADMIN | read_user, create_user, update_user, delete_user |
                                | creator_user | OPERATOR | read_user, create_user, update_user |
                                | reader_user | USER | read_user |
                                
                                ## Endpoints Authorization:
                                - **GET /api/v1/users** - Requires: `read_user` (All roles)
                                - **GET /api/v1/users/{id}** - Requires: `read_user` (All roles)
                                - **POST /api/v1/users** - Requires: `create_user` (ADMIN, OPERATOR)
                                - **PUT /api/v1/users/{id}** - Requires: `update_user` (ADMIN, OPERATOR)
                                - **DELETE /api/v1/users/{id}** - Requires: `delete_user` (ADMIN only)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hamid")
                                .email("hamid@example.com")
                                .url("https://github.com/HamidCodeHub"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Production Server (if applicable)")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
