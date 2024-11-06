package cz.cvut.tjv_backend.dto.group;

import cz.cvut.tjv_backend.dto.userGroup.CreateUserGroupRoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.Group}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateGroupDto implements Serializable {
    private String name;
    private String description;
    private UUID ownerId;
    private Set<CreateUserGroupRoleDto> userRoles = new HashSet<>();

}