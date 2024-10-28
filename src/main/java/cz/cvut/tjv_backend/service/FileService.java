package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.FileOwnershipDetails;
import cz.cvut.tjv_backend.dto.UserDto;
import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.repository.FileRepository;
import cz.cvut.tjv_backend.repository.SharedFileWithGroupRepository;
import cz.cvut.tjv_backend.repository.SharedFileWithUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final SharedFileWithUserRepository sharedFileWithUserRepository;
    private final SharedFileWithGroupRepository sharedFileWithGroupRepository;
    private final UserService userService;

    public File getFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
    }

    public File saveFile(UUID ownerId, MultipartFile file) {
        User user = userService.getUserById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        File newFile = File.builder()
                .owner(user)
                .filename(file.getOriginalFilename())
                .fileType(file.getContentType())
                .size(file.getSize())
                .blobUrl("files/" + file.getOriginalFilename())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();

        return fileRepository.save(newFile);
    }

    public File updateFile(UUID userId, UUID fileId, MultipartFile updatedFile) {
        userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        file.setFilename(updatedFile.getOriginalFilename());
        file.setFileType(updatedFile.getContentType());
        file.setSize(updatedFile.getSize());
        file.setBlobUrl("files/" + updatedFile.getOriginalFilename());
        file.setUpdatedAt(LocalDateTime.now());
        file.setVersion(file.getVersion() + 1);

        return fileRepository.save(file);
    }

    public void deleteFile(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
        fileRepository.delete(file);
    }

    public void deleteFiles(List<UUID> fileIds) {
        List<File> files = fileRepository.findAllById(fileIds);
        if (files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No files found to delete");
        }
        fileRepository.deleteAll(files);
    }

    public List<File> getAllFilesByUser(UUID userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return fileRepository.findFilesByOwnerId(userId);
    }

    public List<File> getFilesNotSharedByUser(UUID userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return fileRepository.findFilesNotShared(userId);
    }

    public Resource getFileAsResource(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
        // TODO: Implement fetching the file data from storage (currently placeholder)
        byte[] data = new byte[0];
        return new ByteArrayResource(data);
    }
    public List<FileOwnershipDetails> getAllFilesOwnedOrSharedWithUser(UUID userId) {
        List<File> files = fileRepository.findAllFilesOwnedOrSharedWithUser(userId);

        return files.stream()
                .map(file -> {
                    String ownershipType;
                    if (file.getOwner().getId().equals(userId)) {
                        ownershipType = "OWNED";
                    } else {
                        ownershipType = "SHARED";
                    }

                    return new FileOwnershipDetails(
                            file,
                            ownershipType,
                            new UserDto(file.getOwner().getId(), file.getOwner().getUsername(), file.getOwner().getEmail())
                    );
                })
                .collect(Collectors.toList());
    }

}