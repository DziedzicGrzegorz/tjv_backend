package cz.cvut.tjv_backend.dto.group;

import cz.cvut.tjv_backend.entity.Group;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link Group}
 */
@Value
public class ShortGroupDto implements Serializable {
    UUID id;
    String name;
    String description;
}