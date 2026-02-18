package com.hamid.usermanagement.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Slf4j
public class UserCreatedEventListener {

    @EventListener
    @Async
    public void handleUserCreatedEvent(UserCreatedEvent event) {

        LocalDateTime eventTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(event.getTimestamp()),
                ZoneId.systemDefault()
        );

        log.info("┌─────────────────────────────────────────┐");
        log.info("│      USER CREATED EVENT RECEIVED        │");
        log.info("├─────────────────────────────────────────┤");
        log.info("│ Event Time: {}", eventTime);
        log.info("│ User ID: {}", event.getUser().getId());
        log.info("│ Username: {}", event.getUser().getUsername());
        log.info("│ Email: {}", event.getUser().getEmail());
        log.info("│ First Name: {}", event.getUser().getFirstName());
        log.info("│ Last Name: {}", event.getUser().getLastName());
        log.info("│ Roles: {}", event.getUser().getRoles());
        log.info("└─────────────────────────────────────────┘");

        // Simula elaborazione asincrona
        try {
            log.info("🔄 Starting async processing for user: {}", event.getUser().getUsername());
            Thread.sleep(2000); // Simula operazione lunga
            log.info("✅ Async processing completed for user: {}", event.getUser().getUsername());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("❌ Error during async processing", e);
        }
    }
}