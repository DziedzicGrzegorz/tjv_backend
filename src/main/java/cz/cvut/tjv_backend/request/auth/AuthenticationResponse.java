package cz.cvut.tjv_backend.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;

    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}
