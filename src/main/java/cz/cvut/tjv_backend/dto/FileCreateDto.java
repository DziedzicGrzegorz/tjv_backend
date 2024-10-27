package cz.cvut.tjv_backend.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Value
@Builder
public class FileCreateDto {
    UUID ownerId;
    String filename;
    String fileType;
    Long size;
    MultipartFile fileContent;
}
