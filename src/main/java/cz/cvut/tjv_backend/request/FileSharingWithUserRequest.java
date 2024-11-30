package cz.cvut.tjv_backend.request;

import cz.cvut.tjv_backend.validation.ValidUUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FileSharingWithUserRequest {
    @NotNull(message = "File ID cannot be null")
    @ValidUUID
    private UUID fileId;

    @NotNull(message = "User ID cannot be null")
    @ValidUUID
    private UUID userId;

    @NotNull(message = "Permission cannot be null")
    @Pattern(regexp = "READ|WRITE", message = "Permission must be one of: READ, WRITE")
    private String permission;
}