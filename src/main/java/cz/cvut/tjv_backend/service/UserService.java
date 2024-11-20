package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.user.UserCreateDto;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.exception.Exceptions;
import cz.cvut.tjv_backend.mapper.UserMapper;
import cz.cvut.tjv_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import cz.cvut.tjv_backend.exception.Exceptions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Create a new User
    public UserDto createUser(UserCreateDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exceptions.NotFoundException("User with this email already exists");
        }
        User newUser = userMapper.toEntity(user);
        User saveduser = userRepository.save(newUser);
        return userMapper.toDto(saveduser);

    }

    // Retrieve a User by ID
    public Optional<UserDto> getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDto);
    }

    // Retrieve a User by email
    public Optional<UserDto> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto);
    }

    // Update a User's username
    public void updateUsername(UUID id, String username) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.updateUsernameById(id, username);
    }

    // Update a User's email
    public void updateEmail(UUID id, String email) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        userRepository.updateEmailById(id, email);
    }

    // Update a User's password
    public void updatePassword(UUID id, String passwordHash) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.updatePasswordById(id, passwordHash);
    }

    // Delete a User by ID
    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    // Delete a User by email
    public void deleteUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteByEmail(email);
    }

    // Retrieve all Users
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        //manually map
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
