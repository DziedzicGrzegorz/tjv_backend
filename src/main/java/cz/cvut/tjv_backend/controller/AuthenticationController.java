package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.user.UserCreateDto;
import cz.cvut.tjv_backend.request.auth.AuthenticationRequest;
import cz.cvut.tjv_backend.request.auth.AuthenticationResponse;
import cz.cvut.tjv_backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserCreateDto request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        service.authenticate(request);
                //check the security context;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
            System.out.println("Details: " + authentication.getDetails());
            System.out.println("Principal: " + authentication.getPrincipal());
        } else {
            System.out.println("No user is authenticated.");
        }


        return ResponseEntity.ok(service.authenticate(request));
    }
    //refresh

}