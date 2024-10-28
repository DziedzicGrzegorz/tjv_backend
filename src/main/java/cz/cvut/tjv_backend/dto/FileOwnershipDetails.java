package cz.cvut.tjv_backend.dto;


import cz.cvut.tjv_backend.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FileOwnershipDetails {
    private UUID fileId;
    private String filename;
    private String blobUrl;
    private String fileType;
    private Long size;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ownershipType;
    private UserDto owner;
    public FileOwnershipDetails(File file, String ownershipType, UserDto owner) {
        this.fileId = file.getId();
        this.filename = file.getFilename();
        this.blobUrl = file.getBlobUrl();
        this.fileType = file.getFileType();
        this.size = file.getSize();
        this.version = file.getVersion();
        this.createdAt = file.getCreatedAt();
        this.updatedAt = file.getUpdatedAt();
        this.ownershipType = ownershipType;
        this.owner = owner;
    }
}
