package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.CreateGroup;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import cz.cvut.tjv_backend.repository.GroupRepository;
import cz.cvut.tjv_backend.repository.UserGroupRoleRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRoleRepository userGroupRoleRepository;

    // Create a new Group
    public Group createGroup(CreateGroup createGroup) {
        Group group = new Group();
        group.setName(createGroup.getName());
        group.setDescription(createGroup.getDescription());
        groupRepository.save(group);

        User founder = userRepository.findById(createGroup.getFounderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Founder not found"));

        UserGroupRole founderRole = UserGroupRole.builder()
                .user(founder)
                .group(group)
                .role(UserGroupRole.Role.FOUNDER)
                .joinedAt(LocalDateTime.now())
                .build();
        userGroupRoleRepository.save(founderRole);

        Set<User> users = createGroup.getUsersToAdd().stream()
                .map(userId -> userRepository.findByEmail(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for email: " + userId)))
                .collect(Collectors.toSet());

        for (User user : users) {
            UserGroupRole userRole = UserGroupRole.builder()
                    .user(user)
                    .group(group)
                    .role(UserGroupRole.Role.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();
            userGroupRoleRepository.save(userRole);
        }

        return groupRepository.save(group);
    }

    // Update a Group
    public Group updateGroup(UUID groupId, Group updatedGroup) {
        return groupRepository.findById(groupId).map(group -> {
            group.setName(updatedGroup.getName());
            group.setDescription(updatedGroup.getDescription());
            return groupRepository.save(group);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
    }

    // Delete a Group by ID
    public void deleteGroup(UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    // Add Users to Group with specific role
    public Group addUsersToGroup(UUID groupId, List<UUID> userIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            if (userGroupRoleRepository.existsByUserAndGroup(user, group)) {
                continue;
            }
            UserGroupRole userRole = UserGroupRole.builder()
                    .user(user)
                    .group(group)
                    .role(UserGroupRole.Role.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();
            userGroupRoleRepository.save(userRole);
        }

        return group;
    }

    // Remove Users from Group
    public Group removeUsersFromGroup(UUID groupId, UUID ownerId, Set<UUID> userIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        if (!userGroupRoleRepository.existsByUserAndGroupAndRole(owner, group, UserGroupRole.Role.ADMIN) &&
                !userGroupRoleRepository.existsByUserAndGroupAndRole(owner, group, UserGroupRole.Role.FOUNDER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only group admins or founders can remove users from the group");
        }

        Set<User> users = new HashSet<>(userRepository.findAllById(userIds));
        for (User user : users) {
            if (userGroupRoleRepository.existsByUserAndGroupAndRole(user, group, UserGroupRole.Role.FOUNDER)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admins cannot remove group founders");
            }
            userGroupRoleRepository.deleteByUserAndGroup(user, group);
        }

        return group;
    }

    // Get a Group by ID
    public  Set<UserGroupRole> getGroupById(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        Set<UserGroupRole> usersGroup = userGroupRoleRepository.findByGroup(group);
        return usersGroup;

    }

    // Get all Groups by User ID
    public List<Group> getAllGroupsByUser(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userGroupRoleRepository.findByUserId(userId).stream()
                .map(UserGroupRole::getGroup)
                .collect(Collectors.toList());
    }

    // Get all Groups
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}
