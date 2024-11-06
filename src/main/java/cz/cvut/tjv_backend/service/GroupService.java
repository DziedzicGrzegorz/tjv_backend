package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.group.CreateGroupDto;
import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.dto.group.GroupUpdateDto;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import cz.cvut.tjv_backend.entity.utils.Role;
import cz.cvut.tjv_backend.mapper.GroupMapper;
import cz.cvut.tjv_backend.repository.GroupRepository;
import cz.cvut.tjv_backend.repository.UserGroupRoleRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final GroupMapper groupMapper;

    // Create a new Group
    @Transactional
    public GroupDto createGroup(CreateGroupDto createGroup) {
        groupRepository.findByName(createGroup.getName())
                .ifPresent(group -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Group with this name already exists");
                });

        Group group = groupMapper.toEntityFromCreateGroupDto(createGroup);

        User founder = userRepository.findById(createGroup.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Founder not found"));

        UserGroupRole founderRole = UserGroupRole.builder()
                .user(founder)
                .group(group)
                .role(Role.FOUNDER)
                .joinedAt(LocalDateTime.now())
                .build();

        group = groupRepository.save(group);
        userGroupRoleRepository.save(founderRole);

        Set<User> users = createGroup.getUserRoles().stream()
                .map(userRoleDto -> userRepository.findById(userRoleDto.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for ID: " + userRoleDto.getId())))
                .collect(Collectors.toSet());

        for (User user : users) {
            UserGroupRole userRole = UserGroupRole.builder()
                    .user(user)
                    .group(group)
                    .role(Role.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();
            userGroupRoleRepository.save(userRole);
        }

        return groupMapper.toDto(group);
    }


    // Update a Group
    public GroupDto updateGroup(GroupUpdateDto updatedGroup) {
        Group existingGroup = groupRepository.findById(updatedGroup.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        Group updatedEntity = Group.builder()
                .id(existingGroup.getId())
                .name(updatedGroup.getName())
                .description(updatedGroup.getDescription())
                .userRoles(existingGroup.getUserRoles())
                .sharedFiles(existingGroup.getSharedFiles())
                .build();

        Group group = groupRepository.save(updatedEntity);
        return groupMapper.toDto(group);
    }

    // Delete a Group by ID
    public void deleteGroup(UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    // Add Users to Group with specific role
    public GroupDto addUsersToGroup(UUID groupId, List<UUID> userIds) {
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
                    .role(Role.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();
            userGroupRoleRepository.save(userRole);
        }

        return groupMapper.toDto(group);
    }

    // Remove Users from Group
    @Transactional
    public GroupDto removeUsersFromGroup(UUID groupId, UUID ownerId, Set<UUID> userIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        if (!userGroupRoleRepository.existsByUserAndGroupAndRole(owner, group, Role.ADMIN) &&
                !userGroupRoleRepository.existsByUserAndGroupAndRole(owner, group, Role.FOUNDER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only group admins or founders can remove users from the group");
        }

        for (UUID userId : userIds) {
            removeUserFromGroup(userId, groupId);
        }
        //get updated grouyp
        group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        return groupMapper.toDto(group);
    }

    // Get a Group by ID
    public GroupDto getGroupById(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        return groupMapper.toDto(group);

    }

    // Get all Groups by User ID
    public List<GroupDto> getAllGroupsByUser(UUID userId) {
        List<Group> group = groupRepository.findAllByUserUserId(userId);
        return group.stream().map(groupMapper::toDto).collect(Collectors.toList());
    }


    // Get all Groups
    public List<GroupDto> getAllGroups() {
        List<Group> group = groupRepository.findAll();
        return group.stream().map(groupMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    protected void removeUserFromGroup(UUID userId, UUID groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (userGroupRoleRepository.existsByUserAndGroup(user, group)) {
            userGroupRoleRepository.deleteByUserIdAndGroupId(user.getId(), group.getId());
        } else {
            throw new IllegalArgumentException("User is not part of this group");
        }
    }
}
