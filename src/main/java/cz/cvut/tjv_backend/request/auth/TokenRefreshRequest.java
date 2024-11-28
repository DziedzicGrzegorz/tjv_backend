package cz.cvut.tjv_backend.request.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
