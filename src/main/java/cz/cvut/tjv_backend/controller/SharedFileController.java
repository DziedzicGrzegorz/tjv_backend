package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.request.FileSharingWithGroupRequest;
import cz.cvut.tjv_backend.request.FileSharingWithUserRequest;
import cz.cvut.tjv_backend.service.SharedFileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shared-files")
@AllArgsConstructor
public class SharedFileController {

    private final SharedFileService sharedFileService;

    // Share a file with a user
    @PostMapping("/user")
    public ResponseEntity<SharedFileWithUserDto> shareFileWithUser(@Valid @RequestBody FileSharingWithUserRequest sharedFileWithUser) {
        SharedFileWithUserDto createdSharedFile = sharedFileService.shareFileWithUser(sharedFileWithUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    // Share a file with a group
    @PostMapping("/group")
    public ResponseEntity<SharedFileWithGroupDto> shareFileWithGroup(@Valid @RequestBody FileSharingWithGroupRequest sharedFileWithGroup) {
        SharedFileWithGroupDto createdSharedFile = sharedFileService.shareFileWithGroup(sharedFileWithGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    // Retrieve shared file with user by ID (file to user)
    @GetMapping("/user/{id}")
    public ResponseEntity<List<SharedFileWithUserDto>> getSharedFileWithUserById(@PathVariable UUID id) {
        List<SharedFileWithUserDto> sharedFileWithUser = sharedFileService.getSharedFilesWithUser(id);
        return ResponseEntity.ok(sharedFileWithUser);
    }

    // Retrieve shared file with group by ID
    @GetMapping("/group/{id}")
    public ResponseEntity<List<SharedFileWithGroupDto>> getSharedFileWithGroupById(@PathVariable UUID id) {
        List<SharedFileWithGroupDto> sharedFileWithGroup = sharedFileService.getSharedFileWithGroupById(id);
        return ResponseEntity.ok(sharedFileWithGroup);
    }

    @DeleteMapping("/user/{userId}/file/{fileId}")
    public ResponseEntity<Void> deleteSharedFileWithUser(
            @PathVariable UUID userId,
            @PathVariable UUID fileId) {
        sharedFileService.deleteSharedFileWithUser(userId, fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/group/{groupId}/file/{fileId}")
    public ResponseEntity<Void> deleteSharedFileWithGroup(
            @PathVariable UUID groupId,
            @PathVariable UUID fileId) {
        sharedFileService.deleteSharedFileWithGroup(groupId, fileId);
        return ResponseEntity.noContent().build();
    }
}
