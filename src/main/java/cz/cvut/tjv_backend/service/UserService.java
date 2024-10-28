package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // Create a new User
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }
        return userRepository.save(user);
    }

    // Retrieve a User by ID
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    // Retrieve a User by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update a User's username
    public void updateUsername(UUID id, String username) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.updateUsernameById(id, username);
    }

    // Update a User's email
    public void updateEmail(UUID id, String email) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }
        userRepository.updateEmailById(id, email);
    }

    // Update a User's password
    public void updatePassword(UUID id, String passwordHash) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.updatePasswordById(id, passwordHash);
    }

    // Delete a User by ID
    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    // Delete a User by email
    public void deleteUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteByEmail(email);
    }

    // Retrieve all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
