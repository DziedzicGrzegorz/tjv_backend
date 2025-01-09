package cz.cvut.tjv_backend.dto;

import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.dto.user.UserShortDto;
import cz.cvut.tjv_backend.entity.SharedFileWithUser;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link SharedFileWithUser}
 */
@Value
public class SharedFileWithUserDto implements Serializable {
    UUID id;
    FileDto file;
    String permission;
    LocalDateTime sharedAt;
    UserShortDto sharedWith;
}