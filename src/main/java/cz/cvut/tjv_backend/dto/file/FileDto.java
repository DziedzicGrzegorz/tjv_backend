package cz.cvut.tjv_backend.dto.file;

import cz.cvut.tjv_backend.dto.user.UserShortDto;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.File}
 */
@Value
@Setter
public class FileDto implements Serializable {
    UUID id;
    UserShortDto owner;
    String filename;
    String fileType;
    Long size;
    Integer version;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}