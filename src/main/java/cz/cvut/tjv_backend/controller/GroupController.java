package cz.cvut.tjv_backend.controller;

import cz.cvut.tjv_backend.dto.CreateGroup;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.UserGroupRole;
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
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroup createGroup) {
        Group createdGroup = groupService.createGroup(createGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    // Retrieve a Group by ID
    @GetMapping("/{groupId}")
    public ResponseEntity<Set<UserGroupRole>> getGroupById(@PathVariable UUID groupId) {
        Set<UserGroupRole> userGroupRoles = groupService.getGroupById(groupId);
        return ResponseEntity.ok(userGroupRoles);
    }


    // Update a Group
    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable UUID groupId, @RequestBody Group updatedGroup) {
        Group group = groupService.updateGroup(groupId, updatedGroup);
        return ResponseEntity.ok(group);
    }

    // Delete a Group by ID
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    // Add Users to Group
    @PutMapping("/{groupId}/add-users")
    public ResponseEntity<Group> addUsersToGroup(@PathVariable UUID groupId, @RequestBody List<UUID> userIds) {
        Group group = groupService.addUsersToGroup(groupId, userIds);
        return ResponseEntity.ok(group);
    }

    // Remove Users from Group
    @PutMapping("/{groupId}/remove-users")
    public ResponseEntity<Group> removeUsersFromGroup(@PathVariable UUID groupId, @RequestBody Set<UUID> userIds, @RequestParam UUID ownerId) {
        Group group = groupService.removeUsersFromGroup(groupId, ownerId, userIds);
        return ResponseEntity.ok(group);
    }

    // Get all Groups by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Group>> getAllGroupsByUser(@PathVariable UUID userId) {
        List<Group> groups = groupService.getAllGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }

    // Get all Groups
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }
}
