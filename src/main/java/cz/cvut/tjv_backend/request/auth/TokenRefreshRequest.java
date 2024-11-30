package cz.cvut.tjv_backend.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token cannot be blank")
    @Size(min = 20, max = 512, message = "Refresh token must be between 20 and 512 characters")
    private String refreshToken;
}
