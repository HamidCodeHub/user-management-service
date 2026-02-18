// src/test/java/com/hamid/usermanagement/service/UserServiceImplTest.java
package com.hamid.usermanagement.service;

import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.dto.request.UpdateUserRequest;
import com.hamid.usermanagement.dto.response.UserResponse;
import com.hamid.usermanagement.entity.Role;
import com.hamid.usermanagement.entity.User;
import com.hamid.usermanagement.event.UserCreatedEvent;
import com.hamid.usermanagement.exception.EmailAlreadyExistsException;
import com.hamid.usermanagement.exception.UserNotFoundException;
import com.hamid.usermanagement.mapper.UserMapper;
import com.hamid.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("test.user")
                .email("test@example.com")
                .taxCode("TSTUSER90A01H501Z")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(Role.DEVELOPER))
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .username("test.user")
                .email("test@example.com")
                .taxCode("TSTUSER90A01H501Z")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(Role.DEVELOPER))
                .build();

        createRequest = new CreateUserRequest();
        createRequest.setUsername("test.user");
        createRequest.setEmail("test@example.com");
        createRequest.setTaxCode("TSTUSER90A01H501Z");
        createRequest.setFirstName("Test");
        createRequest.setLastName("User");
        createRequest.setRoles(Set.of(Role.DEVELOPER));

        updateRequest = new UpdateUserRequest();
        updateRequest.setUsername("updated.user");
        updateRequest.setTaxCode("TSTUSER90A01H501Z");
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setRoles(Set.of(Role.OPERATOR));
    }

    @Test
    @DisplayName("getAllUsers - Should return list of all users")
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("test.user");
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toResponse(any(User.class));
    }

    @Test
    @DisplayName("getUserById - Should return user when found")
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // When
        UserResponse result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("test.user");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getUserById - Should throw exception when user not found")
    void getUserById_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");

        verify(userRepository, times(1)).findById(999L);
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("createUser - Should create user and publish event")
    void createUser_ShouldCreateUserAndPublishEvent() {
        // Given
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(createRequest.getUsername())).thenReturn(false);
        when(userMapper.toEntity(createRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // When
        UserResponse result = userService.createUser(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("test.user");

        // Verify event was published
        ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        UserCreatedEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.getUser()).isEqualTo(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("createUser - Should throw exception when email already exists")
    void createUser_WhenEmailExists_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("test@example.com");

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("createUser - Should throw exception when username already exists")
    void createUser_WhenUsernameExists_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(createRequest.getUsername())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already in use");

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("updateUser - Should update user successfully")
    void updateUser_ShouldUpdateUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        // When
        UserResponse result = userService.updateUser(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw exception when user not found")
    void updateUser_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, updateRequest))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteUser - Should delete user successfully")
    void deleteUser_ShouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser - Should throw exception when user not found")
    void deleteUser_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).deleteById(any());
    }
}