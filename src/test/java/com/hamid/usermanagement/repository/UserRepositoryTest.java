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

        User savedUser = userRepository.save(testUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("test.user");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find user by id")
    void findById_ShouldReturnUser() {

        User savedUser = entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findById(savedUser.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test.user");
    }

    @Test
    @DisplayName("Should check if email exists")
    void existsByEmail_ShouldReturnTrueWhenEmailExists() {

        entityManager.persistAndFlush(testUser);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should check if email does not exist")
    void existsByEmail_ShouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should check if username exists")
    void existsByUsername_ShouldReturnTrueWhenUsernameExists() {

        entityManager.persistAndFlush(testUser);

        boolean exists = userRepository.existsByUsername("test.user");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find user by username")
    void findByUsername_ShouldReturnUser() {

        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByUsername("test.user");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should delete user by id")
    void deleteById_ShouldRemoveUser() {

        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        userRepository.deleteById(userId);
        entityManager.flush();

        Optional<User> deleted = userRepository.findById(userId);
        assertThat(deleted).isEmpty();
    }
}