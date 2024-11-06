package cz.cvut.tjv_backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FileSharingWithGroupRequest {
    private UUID fileId;
    private UUID groupId;
    private String permission;
}
