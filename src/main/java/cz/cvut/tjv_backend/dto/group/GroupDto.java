package cz.cvut.tjv_backend.dto.group;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.UserGroupRoleShortDto;
import cz.cvut.tjv_backend.entity.Group;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link Group}
 */
@Value
public class GroupDto implements Serializable {
    UUID id;
    String name;
    String description;
    Set<UserGroupRoleShortDto> userRoles;
    Set<SharedFileWithGroupDto> sharedFiles;
}