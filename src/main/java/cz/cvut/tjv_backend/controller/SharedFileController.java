package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.request.FileSharingWithGroupRequest;
import cz.cvut.tjv_backend.request.FileSharingWithUserRequest;
import cz.cvut.tjv_backend.service.SharedFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Shared Files", description = "Endpoints for managing file sharing with users and groups")
public class SharedFileController {

    private final SharedFileService sharedFileService;

    @PostMapping("/user")
    @Operation(summary = "Share a file with a user", description = "Shares a specific file with a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "File shared successfully", content = @Content(schema = @Schema(implementation = SharedFileWithUserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<SharedFileWithUserDto> shareFileWithUser(
            @Valid @RequestBody @Parameter(description = "Details of the file and user to share with") FileSharingWithUserRequest sharedFileWithUser) {
        SharedFileWithUserDto createdSharedFile = sharedFileService.shareFileWithUser(sharedFileWithUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }

    @PostMapping("/group")
    @Operation(summary = "Share a file with a group", description = "Shares a specific file with a group.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "File shared successfully", content = @Content(schema = @Schema(implementation = SharedFileWithGroupDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<SharedFileWithGroupDto> shareFileWithGroup(
            @Valid @RequestBody @Parameter(description = "Details of the file and group to share with") FileSharingWithGroupRequest sharedFileWithGroup) {
        SharedFileWithGroupDto createdSharedFile = sharedFileService.shareFileWithGroup(sharedFileWithGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSharedFile);
    }
    @GetMapping("/user")
    @Operation(summary = "Get files shared with a user", description = "Retrieves all files shared with a current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files retrieved successfully", content = @Content(schema = @Schema(implementation = SharedFileWithUserDto[].class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<List<SharedFileWithUserDto>> getSharedFileWithUserByCurrentUser() {
        List<SharedFileWithUserDto> sharedFileWithUser = sharedFileService.getSharedFilesWithCurrentUser();
        return ResponseEntity.ok(sharedFileWithUser);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get files shared with a user", description = "Retrieves all files shared with a specific user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully", content = @Content(schema = @Schema(implementation = SharedFileWithUserDto[].class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<List<SharedFileWithUserDto>> getSharedFileWithUserById(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID id) {
        List<SharedFileWithUserDto> sharedFileWithUser = sharedFileService.getSharedFilesWithUser(id);
        return ResponseEntity.ok(sharedFileWithUser);
    }

    @GetMapping("/group/{id}")
    @Operation(summary = "Get files shared with a group", description = "Retrieves all files shared with a specific group.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully", content = @Content(schema = @Schema(implementation = SharedFileWithGroupDto[].class))),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<List<SharedFileWithGroupDto>> getSharedFileWithGroupById(
            @PathVariable @Parameter(description = "Unique ID of the group") UUID id) {
        List<SharedFileWithGroupDto> sharedFileWithGroup = sharedFileService.getSharedFileWithGroupById(id);
        return ResponseEntity.ok(sharedFileWithGroup);
    }

    @DeleteMapping("/user/{userId}/file/{fileId}")
    @Operation(summary = "Unshare a file from a user", description = "Removes the sharing of a file with a specific user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "File unshared successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "File or user not found", content = @Content)
    })
    public ResponseEntity<Void> deleteSharedFileWithUser(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID userId,
            @PathVariable @Parameter(description = "Unique ID of the file") UUID fileId) {
        sharedFileService.deleteSharedFileWithUser(userId, fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/group/{groupId}/file/{fileId}")
    @Operation(summary = "Unshare a file from a group", description = "Removes the sharing of a file with a specific group.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "File unshared successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "File or group not found", content = @Content)
    })
    public ResponseEntity<Void> deleteSharedFileWithGroup(
            @PathVariable @Parameter(description = "Unique ID of the group") UUID groupId,
            @PathVariable @Parameter(description = "Unique ID of the file") UUID fileId) {
        sharedFileService.deleteSharedFileWithGroup(groupId, fileId);
        return ResponseEntity.noContent().build();
    }
    // New Endpoint 1: Get all files shared by current user with users
    @GetMapping("/user/shared-by-me")
    @Operation(summary = "Get files shared by current user with users", description = "Retrieves all files that the current user has shared with other users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files retrieved successfully", content = @Content(schema = @Schema(implementation = SharedFileWithUserDto[].class))),
            @ApiResponse(responseCode = "404", description = "No shared files found", content = @Content)
    })
    public ResponseEntity<List<SharedFileWithUserDto>> getSharedFilesByCurrentUserWithUsers() {
        List<SharedFileWithUserDto> sharedFiles = sharedFileService.getSharedFilesByCurrentUserWithUsers();
        if (sharedFiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sharedFiles);
    }

    // New Endpoint 2: Get all files shared by current user with groups
    @GetMapping("/group/shared-by-me")
    @Operation(summary = "Get files shared by current user with groups", description = "Retrieves all files that the current user has shared with groups.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files retrieved successfully", content = @Content(schema = @Schema(implementation = SharedFileWithGroupDto[].class))),
            @ApiResponse(responseCode = "404", description = "No shared files found", content = @Content)
    })
    public ResponseEntity<List<SharedFileWithGroupDto>> getSharedFilesByCurrentUserWithGroups() {
        List<SharedFileWithGroupDto> sharedFiles = sharedFileService.getSharedFilesByCurrentUserWithGroups();
        if (sharedFiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sharedFiles);
    }
    // New Endpoint 3: Get all users that a file is shared with
    @GetMapping("/file/{fileId}/users")
    @Operation(summary = "Get users a file is shared with", description = "Retrieves all users that a specific file is shared with.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = UUID[].class))),
            @ApiResponse(responseCode = "404", description = "File not found or not shared", content = @Content)
    })
    public ResponseEntity<List<SharedFileWithUserDto>> getUsersSharedWithFile(
            @PathVariable @Parameter(description = "Unique ID of the file") UUID fileId) {
        List<SharedFileWithUserDto> users = sharedFileService.getSharedUsersByFileId(fileId);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(users);
    }

}