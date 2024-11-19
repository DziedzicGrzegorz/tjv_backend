package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.mapper.FileMapper;
import cz.cvut.tjv_backend.repository.FileRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import cz.cvut.tjv_backend.storage.interfaces.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import cz.cvut.tjv_backend.exception.Exceptions.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserService userService;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public FileDto getFileById(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        return fileMapper.toDto(file);
    }

    public FileDto saveFile(UUID ownerId, MultipartFile file) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UUID fileId = UUID.randomUUID();
        String blobPath = ownerId + "/" + fileId;

        try {
            storageService.uploadFile(blobPath, file);

            File newFile = File.builder()
                    .id(fileId)
                    .owner(user)
                    .filename(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .blobUrl(blobPath)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .version(1)
                    .build();

            File savedFile = fileRepository.save(newFile);

            return fileMapper.toDto(savedFile);
        } catch (StorageUploadException e) {
            throw new BadRequestException("Error uploading file to storage");
        }
    }

    public FileDto updateFile(UUID userId, UUID fileId, MultipartFile updatedFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        if (!file.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("User does not have permission to modify this file");
        }

        String blobPath = userId + "/" + updatedFile.getOriginalFilename();
        try {
            storageService.uploadFile(blobPath, updatedFile);
        } catch (StorageUploadException e) {
            throw new InternalServerException("Error updating file in storage");
        }

        File updateFile = File.builder()
                .id(fileId)
                .filename(updatedFile.getOriginalFilename())
                .fileType(updatedFile.getContentType())
                .size(updatedFile.getSize())
                .blobUrl(blobPath)
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
                .orElseThrow(() -> new NotFoundException("File not found"));
        try {
            storageService.deleteFile(file.getBlobUrl());
        } catch (StorageDeleteException e) {
            throw new BadRequestException("Error deleting file from storage");
        }
        fileRepository.delete(file);
    }

    public void deleteFiles(List<UUID> fileIds) {
        List<File> files = fileRepository.findAllById(fileIds);
        if (files.isEmpty()) {
            throw new NotFoundException("No files found to delete");
        }
        for (File file : files) {
            try {
                storageService.deleteFile(file.getBlobUrl());
            } catch (Exception e) {
                throw new BadRequestException("Error deleting file from storage");
            }
        }
        fileRepository.deleteAll(files);
    }

    public List<FileDto> getAllFilesByUser(UUID userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<File> files = fileRepository.findFilesByOwnerId(userId);
        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FileDto> getFilesNotSharedByUser(UUID userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<File> files = fileRepository.findFilesNotShared(userId);

        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }

    public Resource getFileAsResource(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            storageService.downloadFile(file.getBlobUrl(), outputStream);
            byte[] data = outputStream.toByteArray();
            return new ByteArrayResource(data);
        } catch (Exception e) {
            throw new NotFoundException("Error retrieving file from storage");
        }
    }

    public List<FileDto> getAllFilesOwnedOrSharedWithUser(UUID userId) {
        List<File> files = fileRepository.findAllFilesOwnedOrSharedWithUser(userId);
        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }
}