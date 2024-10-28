package cz.cvut.tjv_backend.dto;

import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link SharedFileWithGroup}
 */
@Value
public class SharedFileWithGroupDto implements Serializable {
    UUID id;
    FileDto file;
    String permission;
    LocalDateTime sharedAt;
}