package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.request.FileSharingWithGroupRequest;
import cz.cvut.tjv_backend.request.FileSharingWithUserRequest;
import cz.cvut.tjv_backend.service.SharedFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/shared-files")
@AllArgsConstructor
public class SharedFileController {

    private final SharedFileService sharedFileService;

    // Share a file with a user
    @PostMapping("/user")
    public ResponseEntity<SharedFileWithUserDto> shareFileWithUser(@RequestBody FileSharingWithUserRequest sharedFileWithUser) {
        SharedFileWithUserDto createdSharedFile = sharedFileService.shareFileWithUser(sharedFileWithUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    // Share a file with a group
    @PostMapping("/group")
    public ResponseEntity<SharedFileWithGroupDto> shareFileWithGroup(@RequestBody FileSharingWithGroupRequest sharedFileWithGroup) {
        SharedFileWithGroupDto createdSharedFile = sharedFileService.shareFileWithGroup(sharedFileWithGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    // Retrieve shared file with user by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<SharedFileWithUserDto> getSharedFileWithUserById(@PathVariable UUID id) {
        SharedFileWithUserDto sharedFileWithUser = sharedFileService.getSharedFileWithUserById(id);
        return ResponseEntity.ok(sharedFileWithUser);
    }

    // Retrieve shared file with group by ID
    @GetMapping("/group/{id}")
    public ResponseEntity<List<SharedFileWithGroupDto>> getSharedFileWithGroupById(@PathVariable UUID id) {
        List<SharedFileWithGroupDto> sharedFileWithGroup = sharedFileService.getSharedFileWithGroupById(id);
        return ResponseEntity.ok(sharedFileWithGroup);
    }

    // Delete shared file with a user
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteSharedFileWithUser(@PathVariable UUID id) {
        sharedFileService.deleteSharedFileWithUser(id);
        return ResponseEntity.noContent().build();
    }

    // Delete shared file with a group
    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteSharedFileWithGroup(@PathVariable UUID id) {
        sharedFileService.deleteSharedFileWithGroup(id);
        return ResponseEntity.noContent().build();
    }

    // Find all files shared with a specific user
    @GetMapping("/user")
    public ResponseEntity<List<SharedFileWithUserDto>> getFilesSharedWithUser(@RequestParam UUID userId) {
        List<SharedFileWithUserDto> sharedFiles = sharedFileService.getFilesSharedWithUser(userId);
        return ResponseEntity.ok(sharedFiles);
    }

    // Find all files shared with a specific group
    @GetMapping("/group")
    public ResponseEntity<List<SharedFileWithGroupDto>> getFilesSharedWithGroup(@RequestParam UUID groupId) {
        List<SharedFileWithGroupDto> sharedFiles = sharedFileService.getFilesSharedWithGroup(groupId);
        return ResponseEntity.ok(sharedFiles);
    }
}
