package cz.cvut.tjv_backend.dto.userGroup;

import cz.cvut.tjv_backend.dto.group.ShortGroupDto;
import cz.cvut.tjv_backend.entity.utils.Role;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.UserGroupRole}
 */
@Value
public class UserGroupRoleDto implements Serializable {
    UUID id;
    ShortGroupDto group;
    Role role;
    LocalDateTime joinedAt;
}