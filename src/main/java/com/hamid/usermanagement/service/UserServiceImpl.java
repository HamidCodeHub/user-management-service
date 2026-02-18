package com.hamid.usermanagement.service;

import com.hamid.usermanagement.dto.request.CreateUserRequest;
import com.hamid.usermanagement.dto.request.UpdateUserRequest;
import com.hamid.usermanagement.dto.response.UserResponse;
import com.hamid.usermanagement.entity.User;
import com.hamid.usermanagement.exception.EmailAlreadyExistsException;
import com.hamid.usermanagement.exception.UserNotFoundException;
import com.hamid.usermanagement.mapper.UserMapper;
import com.hamid.usermanagement.repository.UserRepository;
import com.hamid.usermanagement.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j  // Lombok per logging
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationFacade authenticationFacade;  // ← NUOVO

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        String currentUser = authenticationFacade.getCurrentUsername();
        log.info("User '{}' is retrieving all users", currentUser);

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        String currentUser = authenticationFacade.getCurrentUsername();
        log.info("User '{}' is retrieving user with id: {}", currentUser, id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        String currentUser = authenticationFacade.getCurrentUsername();
        String currentEmail = authenticationFacade.getCurrentUserEmail().orElse("N/A");

        log.info("User '{}' (email: {}) is creating new user with username: {}",
                currentUser, currentEmail, request.getUsername());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already in use: " + request.getUsername());
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        log.info("User '{}' successfully created user with id: {}", currentUser, savedUser.getId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        String currentUser = authenticationFacade.getCurrentUsername();
        log.info("User '{}' is updating user with id: {}", currentUser, id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setUsername(request.getUsername());
        user.setTaxCode(request.getTaxCode());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(request.getRoles());
        }

        User updatedUser = userRepository.save(user);
        log.info("User '{}' successfully updated user with id: {}", currentUser, id);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        String currentUser = authenticationFacade.getCurrentUsername();
        log.info("User '{}' is deleting user with id: {}", currentUser, id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);

        log.info("User '{}' successfully deleted user with id: {}", currentUser, id);
    }
}