package cz.cvut.tjv_backend.dto.userGroup;

import cz.cvut.tjv_backend.entity.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.UserGroupRole}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserGroupRoleDto implements Serializable {
    private String id;

    private Role role;
}