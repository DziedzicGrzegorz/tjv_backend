package cz.cvut.tjv_backend.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.tjv_backend.entity.File}
 */
@Value
@Builder
public class FileDto implements Serializable {
    UserDto owner;
    String filename;
    String fileType;
    Long size;
    UUID id;
    Integer version;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    MultipartFile fileContent;
}