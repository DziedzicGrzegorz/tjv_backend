package cz.cvut.tjv_backend.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.Group}
 */
@Value
public class GroupCreateDto implements Serializable {
    String name;
    String description;
}