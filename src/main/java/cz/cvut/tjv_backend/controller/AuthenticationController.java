package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.request.UserCreateRequest;
import cz.cvut.tjv_backend.request.auth.AuthenticationRequest;
import cz.cvut.tjv_backend.request.auth.AuthenticationResponse;
import cz.cvut.tjv_backend.request.auth.TokenRefreshRequest;
import cz.cvut.tjv_backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user in the system. Requires a unique username and email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Void> register(@Valid @RequestBody UserCreateRequest request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates a user using their username and password. Returns access and refresh tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        AuthenticationResponse response = service.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}