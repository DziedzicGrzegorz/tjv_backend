package cz.cvut.tjv_backend.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.Group}
 */
@Value
public class GroupDto implements Serializable {
     UUID id;
     String name;
     String description;
    Set<UserDto> users;
}