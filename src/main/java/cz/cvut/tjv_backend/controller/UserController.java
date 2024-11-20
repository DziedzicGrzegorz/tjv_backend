package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.user.UserCreateDto;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.request.ChangePassword;
import cz.cvut.tjv_backend.request.UpdateEmail;
import cz.cvut.tjv_backend.request.UpdateUsername;
import cz.cvut.tjv_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // Create a new User
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateDto user) {
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Retrieve a User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Retrieve a User by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update a User's username
    @PutMapping("/{id}/username")
    public ResponseEntity<Void> updateUsername(@PathVariable UUID id, @RequestBody UpdateUsername username) {
        userService.updateUsername(id, username.getUsername());
        return ResponseEntity.noContent().build();
    }

    // Update a User's email
    @PutMapping("/{id}/email")
    public ResponseEntity<Void> updateEmail(@PathVariable UUID id, @RequestBody UpdateEmail email) {
        userService.updateEmail(id, email.getEmail());
        return ResponseEntity.noContent().build();
    }

    // Update a User's password
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @RequestBody ChangePassword passwordHash) {
        userService.updatePassword(id, passwordHash.getPassword());
        return ResponseEntity.noContent().build();
    }

    // Delete a User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Delete a User by email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // Retrieve all Users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
