package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.request.UserCreateRequest;
import cz.cvut.tjv_backend.request.auth.AuthenticationRequest;
import cz.cvut.tjv_backend.request.auth.AuthenticationResponse;
import cz.cvut.tjv_backend.request.auth.TokenRefreshRequest;
import cz.cvut.tjv_backend.service.AuthenticationService;
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
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCreateRequest request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        AuthenticationResponse response = service.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}