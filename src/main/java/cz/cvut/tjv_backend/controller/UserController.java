package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.request.UserCreateRequest;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.request.ChangePasswordRequest;
import cz.cvut.tjv_backend.request.UpdateEmailRequest;
import cz.cvut.tjv_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
@Tag(name = "User Management", description = "Endpoints for managing user accounts")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve user by ID", description = "Fetches user details by their unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                     content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserDto> getUserById(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Retrieve user by email", description = "Fetches user details by their email address.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                     content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable @Parameter(description = "Email address of the user") String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/email")
    @Operation(summary = "Update user email", description = "Updates the email address of a user by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Email updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> updateEmail(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID id,
            @Valid @RequestBody @Parameter(description = "New email address") UpdateEmailRequest email) {
        userService.updateEmail(id, email.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update user password", description = "Updates the password of a user by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> updatePassword(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID id,
            @Valid @RequestBody @Parameter(description = "New password") ChangePasswordRequest passwordHash) {
        userService.updatePassword(id, passwordHash.getPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes a user account by their unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUserById(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/email/{email}")
    @Operation(summary = "Delete user by email", description = "Deletes a user account by their email address.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUserByEmail(
            @PathVariable @Parameter(description = "Email address of the user") String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}