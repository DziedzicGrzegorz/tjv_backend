package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import cz.cvut.tjv_backend.entity.SharedFileWithUser;
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
    public ResponseEntity<SharedFileWithUser> shareFileWithUser(@RequestBody SharedFileWithUser sharedFileWithUser) {
        SharedFileWithUser createdSharedFile = sharedFileService.shareFileWithUser(sharedFileWithUser.getFile(), sharedFileWithUser.getSharedWith(), sharedFileWithUser.getPermission());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    // Share a file with a group
    @PostMapping("/group")
    public ResponseEntity<SharedFileWithGroup> shareFileWithGroup(@RequestBody SharedFileWithGroup sharedFileWithGroup) {
        SharedFileWithGroup createdSharedFile = sharedFileService.shareFileWithGroup(sharedFileWithGroup.getFile(), sharedFileWithGroup.getGroup(), sharedFileWithGroup.getPermission());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    // Retrieve shared file with user by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<SharedFileWithUser> getSharedFileWithUserById(@PathVariable UUID id) {
        Optional<SharedFileWithUser> sharedFileWithUser = sharedFileService.getSharedFileWithUserById(id);
        return sharedFileWithUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Retrieve shared file with group by ID
    @GetMapping("/group/{id}")
    public ResponseEntity<List<SharedFileWithGroupDto>> getSharedFileWithGroupById(@PathVariable UUID id) {
        List<SharedFileWithGroupDto> sharedFileWithGroup = sharedFileService.getSharedFileWithGroupById(id);
        if (sharedFileWithGroup.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shared file not found");
        }
        return ResponseEntity.ok(sharedFileWithGroup);
    }


    // Update permissions for a shared file with a user
    @PutMapping("/user/{id}")
    public ResponseEntity<SharedFileWithUser> updateSharedFileWithUser(@PathVariable UUID id, @RequestParam String newPermission) {
        SharedFileWithUser updatedSharedFile = sharedFileService.updateSharedFileWithUser(id, newPermission);
        return ResponseEntity.ok(updatedSharedFile);
    }

    // Update permissions for a shared file with a group
    @PutMapping("/group/{id}")
    public ResponseEntity<SharedFileWithGroup> updateSharedFileWithGroup(@PathVariable UUID id, @RequestParam String newPermission) {
        SharedFileWithGroup updatedSharedFile = sharedFileService.updateSharedFileWithGroup(id, newPermission);
        return ResponseEntity.ok(updatedSharedFile);
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
    public ResponseEntity<List<SharedFileWithUser>> getFilesSharedWithUser(@RequestParam UUID userId) {
        List<SharedFileWithUser> sharedFiles = sharedFileService.getFilesSharedWithUser(userId);
        return ResponseEntity.ok(sharedFiles);
    }

    // Find all files shared with a specific group
    @GetMapping("/group")
    public ResponseEntity<List<SharedFileWithGroup>> getFilesSharedWithGroup(@RequestParam UUID groupId) {
        List<SharedFileWithGroup> sharedFiles = sharedFileService.getFilesSharedWithGroup(groupId);
        return ResponseEntity.ok(sharedFiles);
    }
}
