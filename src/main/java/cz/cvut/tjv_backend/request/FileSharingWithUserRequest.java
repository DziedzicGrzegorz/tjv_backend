package cz.cvut.tjv_backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FileSharingWithUserRequest {
    private UUID fileId;
    private UUID userId;
    private String permission;
}