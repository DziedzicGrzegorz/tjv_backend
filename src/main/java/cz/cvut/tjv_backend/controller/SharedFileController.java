package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.entity.SharedFile;
import cz.cvut.tjv_backend.service.SharedFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/shared-files")
@AllArgsConstructor
public class SharedFileController {

    private final SharedFileService sharedFileService;

    @GetMapping("/{sharedFileId}")
    public ResponseEntity<SharedFile> getSharedFileById(@PathVariable UUID sharedFileId) {
        Optional<SharedFile> sharedFile = sharedFileService.getSharedFileById(sharedFileId);
        return sharedFile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{ownerId}/file/{fileId}/target/{targetUserId}")
    public ResponseEntity<SharedFile> shareFileWithUser(@PathVariable UUID ownerId, @PathVariable UUID fileId, @PathVariable UUID targetUserId, @RequestParam String permission) {
        SharedFile savedSharedFile = sharedFileService.shareFileWithUser(ownerId, fileId, targetUserId, permission);
        return ResponseEntity.ok(savedSharedFile);
    }

    @DeleteMapping("/{sharedFileId}")
    public ResponseEntity<Void> deleteSharedFile(@PathVariable UUID sharedFileId) {
        sharedFileService.deleteSharedFile(sharedFileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SharedFile>> getSharedFilesByUser(@PathVariable UUID userId) {
        List<SharedFile> sharedFiles = sharedFileService.getFilesSharedWithUser(userId);
        return ResponseEntity.ok(sharedFiles);
    }

    @GetMapping("/groups/user/{userId}")
    public ResponseEntity<List<SharedFile>> getFilesSharedWithGroups(@PathVariable UUID userId) {
        List<SharedFile> sharedFiles = sharedFileService.getFilesSharedWithGroups(userId);
        return ResponseEntity.ok(sharedFiles);
    }
}

