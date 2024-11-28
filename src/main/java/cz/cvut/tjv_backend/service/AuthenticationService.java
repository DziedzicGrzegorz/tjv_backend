package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.user.UserCreateDto;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.exception.Exceptions.InvalidRefreshTokenException;
import cz.cvut.tjv_backend.request.auth.AuthenticationRequest;
import cz.cvut.tjv_backend.request.auth.AuthenticationResponse;
import cz.cvut.tjv_backend.request.auth.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final String USERNAME = "username";

    public void register(UserCreateDto request) {
        userService.createUser(request);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        HashMap<String, Object> claims = new HashMap<>();
        User user = ((User) auth.getPrincipal());
        claims.put(USERNAME, user.getUsername());

        String jwtAccessToken = jwtService.generateAccessToken(claims, (User) auth.getPrincipal());
        String jwtRefreshToken = jwtService.generateRefreshToken(claims, (User) auth.getPrincipal());

        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = (User) userService.loadUserByUsername(username);

        return generateAuthenticationResponse(user);
    }

    private AuthenticationResponse generateAuthenticationResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
