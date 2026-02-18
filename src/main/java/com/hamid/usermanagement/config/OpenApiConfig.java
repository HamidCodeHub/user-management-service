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

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userManagementOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setName("Hamid");
        contact.setEmail("your.email@example.com");

        // Security Scheme per JWT Bearer Token
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token obtained from Keycloak");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        Info info = new Info()
                .title("User Management API")
                .version("1.0.0")
                .description("REST API for managing users with Keycloak JWT authentication")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
    }
}