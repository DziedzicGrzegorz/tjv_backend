package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.request.UserCreateRequest;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.exception.Exceptions.NotFoundException;
import cz.cvut.tjv_backend.exception.Exceptions.UserAlreadyExistsException;
import cz.cvut.tjv_backend.mapper.UserMapper;
import cz.cvut.tjv_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDto createUser(UserCreateRequest userCreateRequestRequest) {
        validateEmailUniqueness(userCreateRequestRequest.getEmail());
        validateUsernameUniqueness(userCreateRequestRequest.getUsername());
        String passwordHash = passwordEncoder.encode(userCreateRequestRequest.getPassword());

        UserCreateRequest userCreateRequest = new UserCreateRequest(userCreateRequestRequest.getUsername(), userCreateRequestRequest.getEmail(), passwordHash);
        User user = userMapper.toEntity(userCreateRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserById(UUID userId) {
        User user = findUserById(userId);
        return userMapper.toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = findUserByEmail(email);
        return userMapper.toDto(user);
    }

    public void updateEmail(UUID userId, String email) {
        validateEmailUniqueness(email);
        ensureUserExists(userId);
        userRepository.updateEmailById(userId, email);
    }

    public void updatePassword(UUID userId, String passwordHash) {
        ensureUserExists(userId);
        String encodedPassword = passwordEncoder.encode(passwordHash);
        userRepository.updatePasswordById(userId, encodedPassword);
    }

    public void deleteUserById(UUID userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
    }

    public void deleteUserByEmail(String email) {
        User user = findUserByEmail(email);
        userRepository.delete(user);
    }

    // Private helper methods

    //@TODO: Should these be public? Why or why not?
    //DOMAIN DRIVEN DESIGN: These methods are private because they are only used internally by the UserService class.
    public User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
    }

    private void validateUsernameUniqueness(String email) {
        if (userRepository.existsByUsername(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
    }

    private void ensureUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
    }
}
