package com.hamid.usermanagement.repository;


import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("test.user")
                .email("test@example.com")
                .taxCode("TSTUSER90A01H501Z")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(Role.DEVELOPER))
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void save_ShouldPersistUser() {
        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("test.user");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find user by id")
    void findById_ShouldReturnUser() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);

        // When
        Optional<User> found = userRepository.findById(savedUser.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test.user");
    }

    @Test
    @DisplayName("Should check if email exists")
    void existsByEmail_ShouldReturnTrueWhenEmailExists() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should check if email does not exist")
    void existsByEmail_ShouldReturnFalseWhenEmailDoesNotExist() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should check if username exists")
    void existsByUsername_ShouldReturnTrueWhenUsernameExists() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        boolean exists = userRepository.existsByUsername("test.user");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find user by username")
    void findByUsername_ShouldReturnUser() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> found = userRepository.findByUsername("test.user");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should delete user by id")
    void deleteById_ShouldRemoveUser() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        // When
        userRepository.deleteById(userId);
        entityManager.flush();

        // Then
        Optional<User> deleted = userRepository.findById(userId);
        assertThat(deleted).isEmpty();
    }
}