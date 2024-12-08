// File: cz.cvut.tjv_backend.controller.GroupController.java

package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.request.CreateGroupRequest;
import cz.cvut.tjv_backend.request.GroupUpdateRequest;
import cz.cvut.tjv_backend.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@AllArgsConstructor
@Tag(name = "Group Management", description = "Endpoints for managing user groups and their memberships")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Create a new group", description = "Creates a new group with the specified details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created successfully", content = @Content(schema = @Schema(implementation = GroupDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<GroupDto> createGroup(
            @Valid @RequestBody @Parameter(description = "Details of the group to create") CreateGroupRequest createGroup
    ) {
        GroupDto createdGroup = groupService.createGroup(createGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Retrieve a group by ID", description = "Fetches the details of a group using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group retrieved successfully", content = @Content(schema = @Schema(implementation = GroupDto.class))),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<GroupDto> getGroupById(
            @PathVariable @Parameter(description = "Unique ID of the group to retrieve") UUID groupId
    ) {
        GroupDto groupDetails = groupService.getGroupById(groupId);
        return ResponseEntity.ok(groupDetails);
    }

    @GetMapping("/name/{groupName}")
    @Operation(summary = "Retrieve a group by name", description = "Fetches the details of a group using its name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group retrieved successfully", content = @Content(schema = @Schema(implementation = GroupDto.class))),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<GroupDto> getGroupByName(
            @PathVariable @Parameter(description = "Name of the group to retrieve") String groupName
    ) {
        GroupDto groupDetails = groupService.getGroupByName(groupName);
        return ResponseEntity.ok(groupDetails);
    }

    @PutMapping
    @Operation(summary = "Update a group", description = "Updates the details of an existing group.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group updated successfully", content = @Content(schema = @Schema(implementation = GroupDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<GroupDto> updateGroup(
            @Valid @RequestBody @Parameter(description = "Updated details of the group") GroupUpdateRequest updatedGroup
    ) {
        GroupDto updatedGroupDetails = groupService.updateGroup(updatedGroup);
        return ResponseEntity.ok(updatedGroupDetails);
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "Delete a group by ID", description = "Deletes a group using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<Void> deleteGroup(
            @PathVariable @Parameter(description = "Unique ID of the group to delete") UUID groupId
    ) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/add-users")
    @Operation(summary = "Add users to a group", description = "Adds users to a group using their IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users added to the group successfully", content = @Content(schema = @Schema(implementation = GroupDto.class))),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<GroupDto> addUsersToGroup(
            @PathVariable @Parameter(description = "Unique ID of the group") UUID groupId,
            @Valid @RequestBody @Parameter(description = "List of user IDs to add to the group") List<UUID> userIds
    ) {
        GroupDto groupWithAddedUsers = groupService.addUsersToGroup(groupId, userIds);
        return ResponseEntity.ok(groupWithAddedUsers);
    }

    @DeleteMapping("/{groupId}/remove-users")
    @Operation(summary = "Remove users from a group", description = "Removes users from a group using their IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users removed from the group successfully", content = @Content(schema = @Schema(implementation = GroupDto.class))),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<GroupDto> removeUsersFromGroup(
            @PathVariable @Parameter(description = "Unique ID of the group") UUID groupId,
            @Valid @RequestBody @Parameter(description = "Set of user IDs to remove from the group") Set<UUID> userIds
    ) {
        GroupDto groupWithRemovedUsers = groupService.removeUsersFromGroup(groupId, userIds);
        return ResponseEntity.ok(groupWithRemovedUsers);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Retrieve all groups by user ID", description = "Fetches all groups associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "User or groups not found", content = @Content)
    })
    public ResponseEntity<List<GroupDto>> getAllGroupsByUser(
            @PathVariable @Parameter(description = "Unique ID of the user") UUID userId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size
    ) {
        List<GroupDto> userGroups = groupService.getAllGroupsByUser(userId, page, size);
        return ResponseEntity.ok(userGroups);
    }

    @GetMapping("/user")
    @Operation(summary = "Retrieve all groups by the current user", description = "Fetches all groups associated with the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "User or groups not found", content = @Content)
    })
    public ResponseEntity<List<GroupDto>> getAllGroupsByCurrentUser(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size
    ) {
        List<GroupDto> userGroups = groupService.getAllGroupsByCurrentUser(page, size);
        return ResponseEntity.ok(userGroups);
    }

    @GetMapping
    @Operation(summary = "Retrieve all groups", description = "Fetches all groups in the system with optional pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups retrieved successfully", content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<GroupDto>> getAllGroups(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size
    ) {
        List<GroupDto> paginatedGroups = groupService.getAllGroups(page, size);
        return ResponseEntity.ok(paginatedGroups);
    }

    @GetMapping("/{groupId}/users")
    @Operation(summary = "Retrieve users of a group", description = "Fetches a paginated list of users belonging to a given group.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<List<UserDto>> getUsersInGroup(
            @PathVariable @Parameter(description = "Unique ID of the group") UUID groupId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size
    ) {
        List<UserDto> users = groupService.getUsersInGroup(groupId, page, size);
        return ResponseEntity.ok(users);
    }
}