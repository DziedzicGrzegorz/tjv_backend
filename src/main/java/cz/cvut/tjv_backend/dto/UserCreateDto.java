package cz.cvut.tjv_backend.dto;

import cz.cvut.tjv_backend.entity.User;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
public class UserCreateDto implements Serializable {
    String username;
    String email;
    String passwordHash;
}
