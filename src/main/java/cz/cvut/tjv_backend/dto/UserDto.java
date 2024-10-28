package cz.cvut.tjv_backend.dto;

import cz.cvut.tjv_backend.entity.User;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto implements Serializable {
    UUID id;
    String username;
    String email;

}