package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import cz.cvut.tjv_backend.entity.SharedFileWithUser;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.mappers.SharedFileWithGroupMapper;
import cz.cvut.tjv_backend.repository.SharedFileWithGroupRepository;
import cz.cvut.tjv_backend.repository.SharedFileWithUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    // Share a file with a user
    public SharedFileWithUser shareFileWithUser(File file, User user, String permission) {
        SharedFileWithUser sharedFileWithUser = SharedFileWithUser.builder()
                .file(file)
                .sharedWith(user)
                .permission(permission)
                .sharedAt(LocalDateTime.now())
                .build();
        return sharedFileWithUserRepository.save(sharedFileWithUser);
    }

    // Share a file with a group
    public SharedFileWithGroup shareFileWithGroup(File file, Group group, String permission) {
        SharedFileWithGroup sharedFileWithGroup = SharedFileWithGroup.builder()
                .file(file)
                .group(group)
                .permission(permission)
                .sharedAt(LocalDateTime.now())
                .build();
        return sharedFileWithGroupRepository.save(sharedFileWithGroup);
    }

    // Retrieve shared file with user by ID
    public Optional<SharedFileWithUser> getSharedFileWithUserById(UUID id) {
        return sharedFileWithUserRepository.findById(id);
    }

    // Updated service method in SharedFileService
    public List<SharedFileWithGroupDto> getSharedFileWithGroupById(UUID id) {
        List<SharedFileWithGroup> sharedFileWithGroup = sharedFileWithGroupRepository.findFilesSharedWithGroup(id);
        if (sharedFileWithGroup.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shared file not found");
        }
        return sharedFileWithGroup.stream()
                .map(sharedFileWithGroupMapper::toDto)
                .collect(Collectors.toList());
    }


    // Update permissions for a shared file with a user
    public SharedFileWithUser updateSharedFileWithUser(UUID id, String newPermission) {
        SharedFileWithUser sharedFileWithUser = sharedFileWithUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedFileWithUser not found"));
        sharedFileWithUser.setPermission(newPermission);
        return sharedFileWithUserRepository.save(sharedFileWithUser);
    }

    // Update permissions for a shared file with a group
    public SharedFileWithGroup updateSharedFileWithGroup(UUID id, String newPermission) {
        SharedFileWithGroup sharedFileWithGroup = sharedFileWithGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedFileWithGroup not found"));
        sharedFileWithGroup.setPermission(newPermission);
        return sharedFileWithGroupRepository.save(sharedFileWithGroup);
    }

    // Delete shared file with a user
    public void deleteSharedFileWithUser(UUID id) {
        SharedFileWithUser sharedFileWithUser = sharedFileWithUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedFileWithUser not found"));
        sharedFileWithUserRepository.delete(sharedFileWithUser);
    }

    // Delete shared file with a group
    public void deleteSharedFileWithGroup(UUID id) {
        SharedFileWithGroup sharedFileWithGroup = sharedFileWithGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedFileWithGroup not found"));
        sharedFileWithGroupRepository.delete(sharedFileWithGroup);
    }

    // Find all files shared with a specific user
    public List<SharedFileWithUser> getFilesSharedWithUser(UUID userId) {
        return sharedFileWithUserRepository.findFilesSharedWithUser(userId);
    }

    // Find all files shared with a specific group
    public List<SharedFileWithGroup> getFilesSharedWithGroup(UUID groupId) {
        return sharedFileWithGroupRepository.findFilesSharedWithGroup(groupId);
    }
}
