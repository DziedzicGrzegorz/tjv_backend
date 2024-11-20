package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.entity.*;
import cz.cvut.tjv_backend.exception.Exceptions.NotFoundException;
import cz.cvut.tjv_backend.mapper.SharedFileWithGroupMapper;
import cz.cvut.tjv_backend.mapper.SharedFileWithUserMapper;
import cz.cvut.tjv_backend.repository.*;
import cz.cvut.tjv_backend.request.FileSharingWithGroupRequest;
import cz.cvut.tjv_backend.request.FileSharingWithUserRequest;
import jakarta.persistence.EntityNotFoundException;
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
    private final SharedFileWithUserMapper sharedFileWithUserMapper;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

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
        // Implement logic to get File and Group entities by IDs from the request
        File file = fileRepository.findById(request.getFileId()).orElseThrow(() -> new EntityNotFoundException("File not found"));
        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(() -> new EntityNotFoundException("Group not found"));

        SharedFileWithGroup sharedFileWithGroup = SharedFileWithGroup.builder()
                .file(file)
                .group(group)
                .permission(request.getPermission())
                .sharedAt(LocalDateTime.now())
                .build();

        SharedFileWithGroup savedSharedFile = sharedFileWithGroupRepository.save(sharedFileWithGroup);
        return sharedFileWithGroupMapper.toDto(savedSharedFile);
    }


    // Retrieve shared file with user by ID
    public Optional<SharedFileWithUserDto> getSharedFileWithUserById(UUID id) {
        Optional<SharedFileWithUser> sharedFileWithUser = sharedFileWithUserRepository.findById(id);
        return sharedFileWithUser.map(sharedFileWithUserMapper::toDto);
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
    public void deleteSharedFileWithUser(UUID id) {
        SharedFileWithUser sharedFileWithUser = sharedFileWithUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SharedFileWithUser not found"));
        sharedFileWithUserRepository.delete(sharedFileWithUser);
    }

    // Delete shared file with a group
    public void deleteSharedFileWithGroup(UUID id) {
        SharedFileWithGroup sharedFileWithGroup = sharedFileWithGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SharedFileWithGroup not found"));
        sharedFileWithGroupRepository.delete(sharedFileWithGroup);
    }

    // Find all files shared with a specific user
    public List<SharedFileWithUserDto> getFilesSharedWithUser(UUID userId) {
        List<SharedFileWithUser> sharedFileWithUser = sharedFileWithUserRepository.findFilesSharedWithUser(userId);
        return sharedFileWithUser.stream()
                .map(sharedFileWithUserMapper::toDto)
                .collect(Collectors.toList());
    }

    // Find all files shared with a specific group
    public List<SharedFileWithGroupDto> getFilesSharedWithGroup(UUID groupId) {
        List<SharedFileWithGroup> sharedFileWithGroups = sharedFileWithGroupRepository.findFilesSharedWithGroup(groupId);
        return sharedFileWithGroups.stream()
                .map(sharedFileWithGroupMapper::toDto)
                .collect(Collectors.toList());
    }
}
