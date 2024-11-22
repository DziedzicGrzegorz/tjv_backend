package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.entity.*;
import cz.cvut.tjv_backend.exception.Exceptions.FileAlreadySharedException;
import cz.cvut.tjv_backend.exception.Exceptions.NotFoundException;
import cz.cvut.tjv_backend.exception.Exceptions.SelfFileShareException;
import cz.cvut.tjv_backend.mapper.SharedFileWithGroupMapper;
import cz.cvut.tjv_backend.mapper.SharedFileWithUserMapper;
import cz.cvut.tjv_backend.repository.*;
import cz.cvut.tjv_backend.request.FileSharingWithGroupRequest;
import cz.cvut.tjv_backend.request.FileSharingWithUserRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SharedFileService {

    private final SharedFileWithUserRepository sharedFileWithUserRepository;
    private final SharedFileWithGroupRepository sharedFileWithGroupRepository;
    private final SharedFileWithGroupMapper sharedFileWithGroupMapper;
    private final SharedFileWithUserMapper sharedFileWithUserMapper;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    // Share a file with a user
    public SharedFileWithUserDto shareFileWithUser(FileSharingWithUserRequest request) {
        // Retrieve the file using the provided file ID
        File file = fileRepository.findById(request.getFileId())
                .orElseThrow(() -> new NotFoundException("File not found"));

        // Retrieve the user using the provided user ID
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        //check if the user wants to share with yourself
        if (file.getOwner().getId().equals(user.getId())) {
            throw new SelfFileShareException("You can't share file with yourself");
        }
        //check if the file is already shared with the user
        Optional<SharedFileWithUser> sharedFileWithUserOptional = sharedFileWithUserRepository.findByFileIdAndSharedWithId(file.getId(), user.getId());

        if (sharedFileWithUserOptional.isPresent()) {
            throw new FileAlreadySharedException("File is already shared with this user");
        }

        // Create and save the shared file entity
        SharedFileWithUser sharedFileWithUser = SharedFileWithUser.builder()
                .file(file)
                .sharedWith(user)
                .permission(request.getPermission())
                .sharedAt(LocalDateTime.now())
                .build();

        SharedFileWithUser savedSharedFile = sharedFileWithUserRepository.save(sharedFileWithUser);

        // Return the saved entity as a DTO
        return sharedFileWithUserMapper.toDto(savedSharedFile);
    }

    // Share a file with a group
    public SharedFileWithGroupDto shareFileWithGroup(FileSharingWithGroupRequest request) {
        File file = getFileById(request.getFileId());
        Group group = getGroupById(request.getGroupId());
        User owner = file.getOwner();

        validateUserMembershipInGroup(owner.getId(), group.getId());
        ensureFileNotAlreadyShared(file.getId(), group.getId());

        SharedFileWithGroup sharedFileWithGroup = createSharedFileWithGroup(file, group, request.getPermission());
        SharedFileWithGroup savedSharedFile = sharedFileWithGroupRepository.save(sharedFileWithGroup);

        return sharedFileWithGroupMapper.toDto(savedSharedFile);
    }

    private File getFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found with ID: " + fileId));
    }

    private Group getGroupById(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + groupId));
    }

    private void validateUserMembershipInGroup(UUID userId, UUID groupId) {
        boolean isMember = userGroupRoleRepository.existsByUserIdAndGroupId(userId, groupId);
        if (!isMember) {
            throw new EntityNotFoundException("User with ID " + userId + " is not a member of group with ID " + groupId);
        }
    }

    private void ensureFileNotAlreadyShared(UUID fileId, UUID groupId) {
        boolean isAlreadyShared = sharedFileWithGroupRepository.existsByFileIdAndGroupId(fileId, groupId);
        if (isAlreadyShared) {
            throw new FileAlreadySharedException("File with ID " + fileId + " is already shared with group ID " + groupId);
        }
    }

    private SharedFileWithGroup createSharedFileWithGroup(File file, Group group, String permission) {
        return SharedFileWithGroup.builder()
                .file(file)
                .group(group)
                .permission(permission)
                .sharedAt(LocalDateTime.now())
                .build();
    }

    // Retrieve shared file with user by ID
    public List<SharedFileWithUserDto> getSharedFilesWithUser(UUID userId) {
        List<SharedFileWithUser> sharedFiles = sharedFileWithUserRepository.findAllBySharedWithId(userId);
        if (sharedFiles.isEmpty()) {
            throw new EntityNotFoundException("No files shared with user ID: " + userId);
        }
        return sharedFiles.stream()
                .map(sharedFileWithUserMapper::toDto)
                .collect(Collectors.toList());
    }


    // Updated service method in SharedFileService
    public List<SharedFileWithGroupDto> getSharedFileWithGroupById(UUID id) {
        List<SharedFileWithGroup> sharedFileWithGroup = sharedFileWithGroupRepository.findFilesSharedWithGroup(id);
        if (sharedFileWithGroup.isEmpty()) {
            throw new EntityNotFoundException("Shared file not found");
        }
        return sharedFileWithGroup.stream()
                .map(sharedFileWithGroupMapper::toDto)
                .collect(Collectors.toList());
    }

    // Delete shared file with a user
    public void deleteSharedFileWithUser(UUID userId, UUID fileId) {
        SharedFileWithUser sharedFile = sharedFileWithUserRepository
                .findByFileIdAndSharedWithId(fileId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shared file not found for fileId: " + fileId + " and userId: " + userId
                ));
        sharedFileWithUserRepository.delete(sharedFile);
    }

    // Delete shared file with a group
    public void deleteSharedFileWithGroup(UUID groupId, UUID fileId) {
        // Retrieve the shared file
        SharedFileWithGroup sharedFile = sharedFileWithGroupRepository
                .findByFileIdAndGroupId(fileId, groupId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shared file not found for fileId: " + fileId + " and groupId: " + groupId));

        sharedFileWithGroupRepository.delete(sharedFile);
    }
}
