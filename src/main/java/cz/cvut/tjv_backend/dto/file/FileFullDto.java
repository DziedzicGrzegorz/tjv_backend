package cz.cvut.tjv_backend.dto.file;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.dto.user.UserShortDto;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.File}
 */
@Value
public class FileFullDto implements Serializable {
    UUID id;
    UserShortDto owner;
    String filename;
    String blobUrl;
    String fileType;
    Long size;
    Integer version;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<SharedFileWithUserDto> sharedWithUsers;
    Set<SharedFileWithGroupDto> sharedWithGroups;
}