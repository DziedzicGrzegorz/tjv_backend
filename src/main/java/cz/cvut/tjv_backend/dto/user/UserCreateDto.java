package cz.cvut.tjv_backend.dto.user;

import cz.cvut.tjv_backend.entity.User;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Value
public class UserCreateDto implements Serializable {
    UUID id;
    String username;
    String email;
    String passwordHash;
}