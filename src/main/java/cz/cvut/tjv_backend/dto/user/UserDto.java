package cz.cvut.tjv_backend.dto.user;

import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.dto.userGroup.UserGroupRoleDto;
import cz.cvut.tjv_backend.entity.utils.UserRole;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.User}
 */
@Value
public class UserDto implements Serializable {
    UUID id;
    String username;
    String email;
    List<UserRole> roles;
    Set<UserGroupRoleDto> groupRoles;
    Set<SharedFileWithUserDto> sharedFiles;
}