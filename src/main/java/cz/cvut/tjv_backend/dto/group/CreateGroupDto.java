package cz.cvut.tjv_backend.dto.group;

import cz.cvut.tjv_backend.dto.userGroup.CreateUserGroupRoleDto;
import cz.cvut.tjv_backend.validation.ValidUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.Group}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateGroupDto implements Serializable {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @ValidUUID(message = "Invalid UUID format for ownerId")
    private String ownerId;

    @Valid
    private Set<CreateUserGroupRoleDto> userRoles = new HashSet<>();
}