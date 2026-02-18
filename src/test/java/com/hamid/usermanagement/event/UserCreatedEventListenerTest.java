package com.hamid.usermanagement.event;

import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@DisplayName("User Created Event Listener Tests (Async)")
@SuppressWarnings("deprecation")
class UserCreatedEventListenerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("test.async")
                .email("test.async@example.com")
                .taxCode("TSTASN90A01H501Z")
                .firstName("Test")
                .lastName("Async")
                .roles(Set.of(Role.DEVELOPER))
                .build();
    }

    @Test
    @DisplayName("Should handle UserCreatedEvent asynchronously")
    void handleUserCreatedEvent_ShouldProcessAsynchronously(CapturedOutput output) {
        // Given
        UserCreatedEvent event = new UserCreatedEvent(this, testUser);

        // When
        long startTime = System.currentTimeMillis();
        eventPublisher.publishEvent(event);
        long publishTime = System.currentTimeMillis() - startTime;

        // Then
        // Event publishing should be fast (< 100ms)
        assertThat(publishTime).isLessThan(100);

        // Wait for async processing to complete (max 3 seconds)
        await()
                .atMost(3, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    String logOutput = output.toString();
                    assertThat(logOutput).contains("USER CREATED EVENT RECEIVED");
                    assertThat(logOutput).contains("test.async");
                    assertThat(logOutput).contains("Async processing completed");
                });
    }

    @Test
    @DisplayName("Should log user details in event")
    void handleUserCreatedEvent_ShouldLogUserDetails(CapturedOutput output) {
        // Given
        UserCreatedEvent event = new UserCreatedEvent(this, testUser);

        // When
        eventPublisher.publishEvent(event);

        // Then
        await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    String logOutput = output.toString();
                    assertThat(logOutput).contains("User ID: 1");
                    assertThat(logOutput).contains("Username: test.async");
                    assertThat(logOutput).contains("Email: test.async@example.com");
                    assertThat(logOutput).contains("DEVELOPER");
                });
    }

}