package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{fileId}")
    public ResponseEntity<FileDto> getFileById(@PathVariable UUID fileId) {
        return ResponseEntity.ok(fileService.getFileById(fileId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(@RequestParam UUID ownerId, @RequestParam MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.saveFile(ownerId, file));
    }

    @PutMapping(value = "/{fileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> updateFile(@PathVariable UUID fileId, @RequestParam UUID userId, @RequestParam MultipartFile updatedFile) {
        return ResponseEntity.ok(fileService.updateFile(userId, fileId, updatedFile));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFiles(@RequestBody List<UUID> fileIds) {
        fileService.deleteFiles(fileIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileDto>> getAllFilesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(fileService.getAllFilesByUser(userId));
    }

    @GetMapping("/user/{userId}/not-shared")
    public ResponseEntity<List<FileDto>> getFilesNotSharedByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(fileService.getFilesNotSharedByUser(userId));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        Resource fileResource = fileService.getFileAsResource(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<FileDto>> getAllFilesOwnedOrSharedWithUser(@PathVariable UUID userId) {
        List<FileDto> fileDetails = fileService.getAllFilesOwnedOrSharedWithUser(userId);
        return ResponseEntity.ok(fileDetails);
    }
}

