package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.group.CreateGroupDto;
import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.dto.group.GroupUpdateDto;
import cz.cvut.tjv_backend.dto.userGroup.CreateUserGroupRoleDto;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import cz.cvut.tjv_backend.entity.utils.Role;
import cz.cvut.tjv_backend.exception.Exceptions.*;
import cz.cvut.tjv_backend.mapper.GroupMapper;
import cz.cvut.tjv_backend.repository.GroupRepository;
import cz.cvut.tjv_backend.repository.UserGroupRoleRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupDto createGroup(CreateGroupDto createGroupDto) {
        validateGroupNameUniqueness(createGroupDto.getName());

        Group group = groupMapper.toEntityFromCreateGroupDto(createGroupDto);
        User founder = getUserById(createGroupDto.getOwnerId());

        group = groupRepository.save(group);
        assignUserRoleToGroup(founder, group, Role.FOUNDER);

        List<User> members = getUsersByIds(createGroupDto.getUserRoles().stream()
                .map(CreateUserGroupRoleDto::getId)
                .collect(Collectors.toSet()));

        assignUsersToGroup(members, group, Role.MEMBER);

        Group newGroup = getGroupByIdEntity(group.getId());

        return groupMapper.toDto(newGroup);
    }

    public GroupDto updateGroup(GroupUpdateDto groupUpdateDto) {
        Group existingGroup = getGroupByIdEntity(groupUpdateDto.getId());


        Group updatedEntity = Group.builder()
                .id(existingGroup.getId())
                .name(groupUpdateDto.getName())
                .description(groupUpdateDto.getDescription())
                .userRoles(existingGroup.getUserRoles())
                .sharedFiles(existingGroup.getSharedFiles())
                .build();

        Group updatedGroup = groupRepository.save(updatedEntity);
        return groupMapper.toDto(updatedGroup);
    }

    public void deleteGroup(UUID groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException("Group not found with ID: " + groupId);
        }
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public GroupDto addUsersToGroup(UUID groupId, List<UUID> userIds) {
        Group group = getGroupByIdEntity(groupId);
        List<User> users = getUsersByIds(userIds);

        User founder = getGroupFounder(group);

        for (User user : users) {
            if (userGroupRoleRepository.existsByUserAndGroup(user, group)) {
                throw new UserAlreadyMemberException("User " + user.getUsername() + " is already a member of the group");
            }
            if (user.getId().equals(founder.getId())) {
                throw new GroupOwnerCannotBeMemberException("Group owner cannot add themselves as a member");
            }
            assignUserRoleToGroup(user, group, Role.MEMBER);
        }
        return groupMapper.toDto(group);
    }

    @Transactional
    public GroupDto removeUsersFromGroup(UUID groupId, Set<UUID> userIds) {

        for (UUID userId : userIds) {
            UserGroupRole userRole = userGroupRoleRepository.findByUserIdAndGroupId(userId, groupId)
                    .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found in group"));
            if (userRole.getRole() == Role.FOUNDER) {
                throw new ForbiddenOperationException("Cannot remove the founder from the group");
            }
            userGroupRoleRepository.deleteByUserIdAndGroupId(userId, groupId);
        }
        Group NewGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));
        return groupMapper.toDto(NewGroup);
    }

    public GroupDto getGroupById(UUID groupId) {
        Group group = getGroupByIdEntity(groupId);
        return groupMapper.toDto(group);
    }

    public List<GroupDto> getAllGroupsByUser(UUID userId) {
        List<Group> groups = groupRepository.findAllByUserUserId(userId);
        return groups.stream().map(groupMapper::toDto).collect(Collectors.toList());
    }

    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(groupMapper::toDto).collect(Collectors.toList());
    }

    // Private helper methods

    private void validateGroupNameUniqueness(String groupName) {
        if (groupRepository.existsByName(groupName)) {
            throw new GroupAlreadyExistsException("Group with name '" + groupName + "' already exists");
        }
    }

    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    private Group getGroupByIdEntity(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found with ID: " + groupId));
    }

    private List<User> getUsersByIds(Collection<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            Set<UUID> foundUserIds = users.stream().map(User::getId).collect(Collectors.toSet());
            Set<UUID> missingUserIds = new HashSet<>(userIds);
            missingUserIds.removeAll(foundUserIds);
            throw new NotFoundException("Users not found with IDs: " + missingUserIds);
        }
        return users;
    }

    private void assignUserRoleToGroup(User user, Group group, Role role) {
        UserGroupRole userGroupRole = UserGroupRole.builder()
                .user(user)
                .group(group)
                .role(role)
                .joinedAt(LocalDateTime.now())
                .build();
        userGroupRoleRepository.save(userGroupRole);
        // Update the group's userRoles collection
        group.getUserRoles().add(userGroupRole);
    }

    private void assignUsersToGroup(Collection<User> users, Group group, Role role) {
        for (User user : users) {
            assignUserRoleToGroup(user, group, role);
        }
    }

    private User getGroupFounder(Group group) {
        return group.getUserRoles().stream()
                .filter(userGroupRole -> userGroupRole.getRole() == Role.FOUNDER)
                .map(UserGroupRole::getUser)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Group founder not found"));
    }
}
