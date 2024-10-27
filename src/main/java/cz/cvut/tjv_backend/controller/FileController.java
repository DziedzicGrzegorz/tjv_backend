package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.FileDto;
import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.mappers.FileMapper;
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
    private final FileMapper fileMapper;


    @GetMapping(value = "/{fileId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        File file = fileService.getFileById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));

        Resource fileResource = fileService.getFileAsResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header("File-ID", file.getId().toString())
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .body(fileResource);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<FileDto> uploadFile(
            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("file") MultipartFile fileToUpload) {

        File savedFile = fileService.saveFile(ownerId, fileToUpload);
        FileDto response = fileMapper.toDto(savedFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{fileId}", consumes = {"multipart/form-data"})
    public ResponseEntity<FileDto> updateFile(@PathVariable UUID fileId,
                                              @RequestParam("file") MultipartFile fileToUpdate,
                                              @RequestParam("ownerId") UUID ownerId) {


        File updatedFile = fileService.updateFile(ownerId, fileId, fileToUpdate);
        FileDto response = fileMapper.toDto(updatedFile);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileDto>> getFilesOwnedByUser(@PathVariable UUID userId) {
        List<File> files = fileService.getFilesOwnedByUser(userId);
        List<FileDto> fileDtos = files.stream().map(fileMapper::toDto).toList();
        return ResponseEntity.ok(fileDtos);
    }
}

