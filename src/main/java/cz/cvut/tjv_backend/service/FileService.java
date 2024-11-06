package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.mapper.FileMapper;
import cz.cvut.tjv_backend.repository.FileRepository;
import cz.cvut.tjv_backend.repository.SharedFileWithGroupRepository;
import cz.cvut.tjv_backend.repository.SharedFileWithUserRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
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
    private final FileMapper fileMapper;
    private final UserRepository userRepository;

    public FileDto getFileById(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        return fileMapper.toDto(file);
    }

    public FileDto saveFile(UUID ownerId, MultipartFile file) {
        User user = userRepository.findById(ownerId)
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

        File savedFile = fileRepository.save(newFile);
        return fileMapper.toDto(savedFile);
    }

    public FileDto updateFile(UUID userId, UUID fileId, MultipartFile updatedFile) {
        userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));


        File updateFile = File.builder()
                .id(fileId)
                .filename(updatedFile.getOriginalFilename())
                .fileType(updatedFile.getContentType())
                .size(updatedFile.getSize())
                .blobUrl("files/" + updatedFile.getOriginalFilename())
                .owner(file.getOwner())
                .createdAt(file.getCreatedAt())
                .sharedWithUsers(file.getSharedWithUsers())
                .sharedWithGroups(file.getSharedWithGroups())
                .updatedAt(LocalDateTime.now())
                .version(file.getVersion() + 1)
                .build();
        File savedFile = fileRepository.save(updateFile);
        return fileMapper.toDto(savedFile);
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

    public List<FileDto> getAllFilesByUser(UUID userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<File> files = fileRepository.findFilesByOwnerId(userId);
        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FileDto> getFilesNotSharedByUser(UUID userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<File> files = fileRepository.findFilesNotShared(userId);

        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }

    public Resource getFileAsResource(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
        // TODO: Implement fetching the file data from storage (currently placeholder)
        byte[] data = "sample file content".getBytes();
        return new ByteArrayResource(data);
    }

    public List<FileDto> getAllFilesOwnedOrSharedWithUser(UUID userId) {
        List<File> files = fileRepository.findAllFilesOwnedOrSharedWithUser(userId);
        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());

    }
}