package com.hamid.usermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

        Info info = new Info()
                .title("User Management API")
                .version("1.0.0")
                .description("REST API for managing users with role-based access control")
                .contact(contact)
                .license(new License().name("MIT License"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}