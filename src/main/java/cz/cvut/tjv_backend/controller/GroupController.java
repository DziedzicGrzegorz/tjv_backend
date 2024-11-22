package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.group.CreateGroupDto;
import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.dto.group.GroupUpdateDto;
import cz.cvut.tjv_backend.service.GroupService;
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
public class GroupController {

    private final GroupService groupService;

    // Create a new Group
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody CreateGroupDto createGroup) {
        GroupDto createdGroup = groupService.createGroup(createGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    // Retrieve a Group by ID
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable UUID groupId) {
        GroupDto userGroupRoles = groupService.getGroupById(groupId);
        return ResponseEntity.ok(userGroupRoles);
    }


    // Update a Group
    @PutMapping("/")
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupUpdateDto updatedGroup) {
        GroupDto group = groupService.updateGroup(updatedGroup);
        return ResponseEntity.ok(group);
    }

    // Delete a Group by ID
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    // Add Users to Group
    @PostMapping("/{groupId}/add-users")
    public ResponseEntity<GroupDto> addUsersToGroup(@PathVariable UUID groupId, @RequestBody List<UUID> userIds) {
        GroupDto group = groupService.addUsersToGroup(groupId, userIds);
        return ResponseEntity.ok(group);
    }

    // Remove Users from Group
    @DeleteMapping("/{groupId}/remove-users")
    public ResponseEntity<GroupDto> removeUsersFromGroup(@PathVariable UUID groupId, @RequestBody Set<UUID> userIds) {
        GroupDto group = groupService.removeUsersFromGroup(groupId, userIds);
        return ResponseEntity.ok(group);
    }

    // Get all Groups by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupDto>> getAllGroupsByUser(@PathVariable UUID userId) {
        List<GroupDto> groups = groupService.getAllGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }

    // Get all Groups
    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }
}
