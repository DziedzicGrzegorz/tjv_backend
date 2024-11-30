package cz.cvut.tjv_backend.dto.group;

import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.validation.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Group}
 */
@Value
public class GroupUpdateDto implements Serializable {
    @ValidUUID(message = "Invalid UUID format for ID")
    String id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description;
}