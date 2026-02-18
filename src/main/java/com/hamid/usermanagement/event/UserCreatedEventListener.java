package com.hamid.usermanagement.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreatedEventListener {

    @EventListener
    @Async
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("=== USER CREATED EVENT ===");
        log.info("Event received at: {}", event.getTimestamp());
        log.info("User ID: {}", event.getUser().getId());
        log.info("Username: {}", event.getUser().getUsername());
        log.info("Email: {}", event.getUser().getEmail());
        log.info("Created by: {}", event.getCreatedBy());
        log.info("Roles: {}", event.getUser().getRoles());
        log.info("==========================");

        // Qui potresti:
        // - Inviare email di benvenuto
        // - Notificare admin
        // - Aggiornare statistiche
        // - Inserire in coda di audit
        // - Sincronizzare con altri sistemi

        // Simuliamo un'operazione asincrona
        try {
            Thread.sleep(1000);
            log.info("Async processing completed for user: {}", event.getUser().getUsername());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}