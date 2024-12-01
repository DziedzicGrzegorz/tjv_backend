package cz.cvut.tjv_backend.dto;

import cz.cvut.tjv_backend.entity.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.UserGroupRole}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserGroupRoleDto implements Serializable {
    private UUID id;

    private Role role;
}