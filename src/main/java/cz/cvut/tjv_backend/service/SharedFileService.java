package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.SharedFile;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.repository.FileRepository;
import cz.cvut.tjv_backend.repository.SharedFileRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SharedFileService {

    private SharedFileRepository sharedFileRepository;

    private FileRepository fileRepository;

    private UserRepository userRepository;

    @Transactional
    public SharedFile shareFileWithUser(UUID ownerId, UUID fileId, UUID targetUserId, String permission) {
        // Verify that the file exists and is owned by the requesting user
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isEmpty() || !fileOptional.get().getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("File not found or user is not the owner");
        }

        // Verify that the target user exists
        Optional<User> targetUserOptional = userRepository.findById(targetUserId);
        if (targetUserOptional.isEmpty()) {
            throw new IllegalArgumentException("Target user not found");
        }

        // Check if the file is already shared with the target user
        boolean alreadyShared = sharedFileRepository.existsByFileIdAndSharedWithId(fileId, targetUserId);
        if (alreadyShared) {
            throw new IllegalArgumentException("File is already shared with this user");
        }

        SharedFile sharedFile = SharedFile.builder()
                .file(fileOptional.get())
                .sharedWith(targetUserOptional.get())
                .permission(permission)
                .sharedAt(LocalDateTime.now())
                .build();

        return sharedFileRepository.save(sharedFile);
    }

    public List<SharedFile> getFilesSharedWithUser(UUID userId) {
        return sharedFileRepository.findBySharedWithId(userId);
    }

    public List<SharedFile> getFilesSharedWithGroups(UUID userId) {
        return sharedFileRepository.findFilesSharedWithGroupsByUser(userId);
    }

    public void deleteSharedFile(UUID sharedFileId) {
        sharedFileRepository.deleteById(sharedFileId);
    }

    public Optional<SharedFile> getSharedFileById(UUID sharedFileId) {
        return sharedFileRepository.findById(sharedFileId);
    }
}
