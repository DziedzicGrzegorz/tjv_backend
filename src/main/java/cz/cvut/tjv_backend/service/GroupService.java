package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.group.CreateGroupDto;
import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.dto.group.GroupUpdateDto;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import cz.cvut.tjv_backend.entity.utils.Role;
import cz.cvut.tjv_backend.exception.Exceptions.ForbiddenOperationException;
import cz.cvut.tjv_backend.exception.Exceptions.GroupOwnerCannotBeMemberException;
import cz.cvut.tjv_backend.exception.Exceptions.NotFoundException;
import cz.cvut.tjv_backend.exception.Exceptions.UserAlreadyMemberException;
import cz.cvut.tjv_backend.mapper.GroupMapper;
import cz.cvut.tjv_backend.repository.GroupRepository;
import cz.cvut.tjv_backend.repository.UserGroupRoleRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    @PersistenceContext
    private EntityManager entityManager;

    // Create a new Group
    @Transactional
    public GroupDto createGroup(CreateGroupDto createGroup) {
        groupRepository.findByName(createGroup.getName())
                .ifPresent(group -> {
                    throw new NotFoundException("Group with this name already exists");
                });

        Group group = groupMapper.toEntityFromCreateGroupDto(createGroup);

        User founder = userRepository.findById(createGroup.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Founder not found"));

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
                        .orElseThrow(() -> new NotFoundException("User not found for ID: " + userRoleDto.getId())))
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
        this.entityManager.flush();
        this.entityManager.clear();
        Group newGroup = groupRepository.findById(group.getId())
                .orElseThrow(() -> new NotFoundException("Group not found"));

        return groupMapper.toDto(newGroup);
    }


    // Update a Group
    public GroupDto updateGroup(GroupUpdateDto updatedGroup) {
        Group existingGroup = groupRepository.findById(updatedGroup.getId())
                .orElseThrow(() -> new NotFoundException("Group not found"));

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
    @Transactional
    public GroupDto addUsersToGroup(UUID groupId, List<UUID> userIds) {
        Group group = this.groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found"));
        List<User> users = this.userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            for (UUID userId : userIds) {
                if (users.stream().noneMatch((userx) -> userx.getId().equals(userId))) {
                    throw new NotFoundException("User with ID " + userId + " not found");
                }
            }
        }

        User owner = group.getUserRoles().stream().filter((userRolex) -> userRolex.getRole() == Role.FOUNDER).findFirst().map(UserGroupRole::getUser).orElseThrow(() -> new NotFoundException("Group owner not found"));

        for (User user : users) {
            if (this.userGroupRoleRepository.existsByUserAndGroup(user, group)) {
                throw new UserAlreadyMemberException("User " + user.getUsername() + " is already a member of the group");
            }

            if (user.getId().equals(owner.getId())) {
                throw new GroupOwnerCannotBeMemberException("Group owner cannot add themselves as a member");
            }

            UserGroupRole userRole = UserGroupRole.builder().user(user).group(group).role(Role.MEMBER).joinedAt(LocalDateTime.now()).build();
            this.userGroupRoleRepository.save(userRole);
        }
        this.entityManager.flush();
        this.entityManager.clear();
        Group NewGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));
        return groupMapper.toDto(NewGroup);
    }

    @Transactional
    public GroupDto removeUsersFromGroup(UUID groupId, Set<UUID> userIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        boolean isAdminOrFounder = group.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRole() == Role.FOUNDER || userRole.getRole() == Role.ADMIN);

        if (!isAdminOrFounder) {
            throw new ForbiddenOperationException("Only group admins or founders can remove users from the group");
        }

        userIds.forEach(userId -> {
            UserGroupRole userRole = userGroupRoleRepository.findByUserIdAndGroupId(userId, group.getId())
                    .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found in group"));
            if (userRole.getRole() == Role.FOUNDER) {
                throw new ForbiddenOperationException("Cannot remove the founder from the group");
            }
            userGroupRoleRepository.deleteByUserIdAndGroupId(userId, groupId);
        });

        entityManager.flush();
        entityManager.clear();

        Group NewGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));
        return groupMapper.toDto(NewGroup);
    }


    // Get a Group by ID
    public GroupDto getGroupById(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found"));
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
}
