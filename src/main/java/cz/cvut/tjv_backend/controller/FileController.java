package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "File Management", description = "Endpoints for managing files and their metadata")
public class FileController {

    private final FileService fileService;

    @GetMapping("/{fileId}")
    @Operation(summary = "Get file by ID", description = "Retrieve file metadata by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File retrieved successfully",
                         content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public ResponseEntity<FileDto> getFileById(@PathVariable UUID fileId) {
        return ResponseEntity.ok(fileService.getFileById(fileId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file", description = "Upload a file with its content and metadata.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully",
                         content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<FileDto> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.saveFile(file));
    }

    @PutMapping(value = "/{fileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update a file", description = "Update an existing file's content and metadata.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated successfully",
                         content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<FileDto> updateFile(@PathVariable UUID fileId, @RequestParam UUID userId, @RequestParam MultipartFile updatedFile) {
        return ResponseEntity.ok(fileService.updateFile(userId, fileId, updatedFile));
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete file by ID", description = "Delete a file using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete multiple files", description = "Delete multiple files by their IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Files deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid file IDs", content = @Content)
    })
    public ResponseEntity<Void> deleteFiles(@Valid @RequestBody List<UUID> fileIds) {
        fileService.deleteFiles(fileIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    @Operation(summary = "Get all files by user", description = "Retrieve all files owned by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
                         content = @Content(schema = @Schema(implementation = FileDto[].class))),
            @ApiResponse(responseCode = "404", description = "User not found or no files", content = @Content)
    })
    public ResponseEntity<List<FileDto>> getAllFilesByUser() {
        return ResponseEntity.ok(fileService.getAllFilesByUser());
    }

    @GetMapping("/user/{userId}/not-shared")
    @Operation(summary = "Get non-shared files", description = "Retrieve all files owned by a user that are not shared.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
                         content = @Content(schema = @Schema(implementation = FileDto[].class))),
            @ApiResponse(responseCode = "404", description = "User not found or no files", content = @Content)
    })
    public ResponseEntity<List<FileDto>> getFilesNotSharedByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(fileService.getFilesNotSharedByUser(userId));
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "Download a file", description = "Download a file by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully",
                         content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        Resource fileResource = fileService.getFileAsResource(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "Get all files for a user", description = "Retrieve all files owned or shared with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
                         content = @Content(schema = @Schema(implementation = FileDto[].class))),
            @ApiResponse(responseCode = "404", description = "User not found or no files", content = @Content)
    })
    public ResponseEntity<List<FileDto>> getAllFilesOwnedOrSharedWithUser(@PathVariable UUID userId) {
        List<FileDto> fileDetails = fileService.getAllFilesOwnedOrSharedWithUser(userId);
        return ResponseEntity.ok(fileDetails);
    }
}