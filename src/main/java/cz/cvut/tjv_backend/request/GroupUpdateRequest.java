package cz.cvut.tjv_backend.request;

import cz.cvut.tjv_backend.entity.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link Group}
 */
@Value
public class GroupUpdateRequest implements Serializable {
    @NotNull(message = "Owner ID cannot be null")
    UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description;
}