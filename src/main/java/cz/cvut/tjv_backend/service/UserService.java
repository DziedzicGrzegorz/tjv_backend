package cz.cvut.tjv_backend.service;


import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
