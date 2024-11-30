package cz.cvut.tjv_backend.dto.user;

import cz.cvut.tjv_backend.entity.User;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
public class UserCreateDto implements Serializable {
    @NotBlank(message = "Username is mandatory")
    @Size(min = 7, max = 50, message = "Username must be between 3 and 50 characters")
    String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be a valid format")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    String password;
}