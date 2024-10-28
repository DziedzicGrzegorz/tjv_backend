package cz.cvut.tjv_backend.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.Group}
 */
@Value
public class GroupCreateDto implements Serializable {
    String name;
    String description;
    UUID founderId;
    Set<String> usersToAdd;
}