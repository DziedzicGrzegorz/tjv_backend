package cz.cvut.tjv_backend.dto;

import cz.cvut.tjv_backend.entity.utils.Role;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.UserGroupRole}
 */
@Value
public class UserGroupRoleShortDto implements Serializable {
    UUID id;
    Role role;
    LocalDateTime joinedAt;
}