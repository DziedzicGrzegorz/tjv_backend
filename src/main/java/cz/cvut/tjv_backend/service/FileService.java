package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.exception.Exceptions.*;
import cz.cvut.tjv_backend.mapper.FileMapper;
import cz.cvut.tjv_backend.repository.FileRepository;
import cz.cvut.tjv_backend.storage.interfaces.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserService userService;
    private final FileMapper fileMapper;
    private final StorageService storageService;

    public FileDto getFileById(UUID fileId) {
        File file = findFileById(fileId);
        return fileMapper.toDto(file);
    }

    public FileDto saveFile(MultipartFile file) {
        UserDto currentUser = userService.getCurrentUser();
        UUID ownerId = currentUser.getId();
        User owner = userService.findUserById(ownerId);
        UUID fileId = UUID.randomUUID();
        String blobPath = buildBlobPath(ownerId, fileId);

        uploadFileToStorage(blobPath, file);

        File newFile = buildFileEntity(fileId, owner, file, blobPath, 1);
        File savedFile = fileRepository.save(newFile);

        return fileMapper.toDto(savedFile);
    }

    public FileDto updateFile(UUID userId, UUID fileId, MultipartFile updatedFile) {
        File existingFile = findFileById(fileId);

        validateFileOwnership(existingFile, userId);

        String blobPath = buildBlobPath(userId, fileId);
        uploadFileToStorage(blobPath, updatedFile);

        File updatedFileEntity = buildUpdatedFileEntity(existingFile, updatedFile, blobPath);
        File savedFile = fileRepository.save(updatedFileEntity);

        return fileMapper.toDto(savedFile);
    }

    public void deleteFile(UUID fileId) {
        File file = findFileById(fileId);
        deleteFileFromStorage(file.getBlobUrl());
        fileRepository.delete(file);
    }

    public void deleteFiles(List<UUID> fileIds) {
        List<File> files = fileRepository.findAllById(fileIds);
        if (files.isEmpty()) {
            throw new NotFoundException("No files found to delete");
        }
        for (File file : files) {
            deleteFileFromStorage(file.getBlobUrl());
        }
        fileRepository.deleteAll(files);
    }

    public List<FileDto> getAllFilesByUser() {
        UserDto currentUser = userService.getCurrentUser();
        UUID userId = currentUser.getId();
        List<File> files = fileRepository.findFilesByOwnerId(userId);
        return mapFilesToDto(files);
    }

    public List<FileDto> getFilesNotSharedByUser(UUID userId) {
        userService.getUserById(userId);
        List<File> files = fileRepository.findFilesNotShared(userId);
        return mapFilesToDto(files);
    }

    public Resource getFileAsResource(UUID fileId) {
        File file = findFileById(fileId);
        byte[] data = downloadFileFromStorage(file.getBlobUrl());
        return new ByteArrayResource(data);
    }

    public List<FileDto> getAllFilesOwnedOrSharedWithUser(UUID userId) {
        List<File> files = fileRepository.findAllFilesOwnedOrSharedWithUser(userId);
        return mapFilesToDto(files);
    }

    private File findFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found with ID: " + fileId));
    }

    private String buildBlobPath(UUID ownerId, UUID fileId) {
        return ownerId + "/" + fileId;
    }

    private void uploadFileToStorage(String blobPath, MultipartFile file) {
        try {
            storageService.uploadFile(blobPath, file);
        } catch (StorageUploadException e) {
            throw new BadRequestException("Error uploading file to storage");
        }
    }

    private File buildFileEntity(UUID fileId, User owner, MultipartFile file, String blobPath, int version) {
        return File.builder()
                .id(fileId)
                .owner(owner)
                .filename(file.getOriginalFilename())
                .fileType(file.getContentType())
                .size(file.getSize())
                .blobUrl(blobPath)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(version)
                .build();
    }

    private File buildUpdatedFileEntity(File existingFile, MultipartFile updatedFile, String blobPath) {
        return File.builder()
                .id(existingFile.getId())
                .owner(existingFile.getOwner())
                .filename(updatedFile.getOriginalFilename())
                .fileType(updatedFile.getContentType())
                .size(updatedFile.getSize())
                .blobUrl(blobPath)
                .createdAt(existingFile.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .version(existingFile.getVersion() + 1)
                .sharedWithUsers(existingFile.getSharedWithUsers())
                .sharedWithGroups(existingFile.getSharedWithGroups())
                .build();
    }

    private void validateFileOwnership(File file, UUID userId) {
        if (!file.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("User does not have permission to modify this file");
        }
    }

    private void deleteFileFromStorage(String blobUrl) {
        try {
            storageService.deleteFile(blobUrl);
        } catch (StorageDeleteException e) {
            throw new BadRequestException("Error deleting file from storage");
        }
    }

    private byte[] downloadFileFromStorage(String blobUrl) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            storageService.downloadFile(blobUrl, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new NotFoundException("Error retrieving file from storage");
        }
    }

    private List<FileDto> mapFilesToDto(List<File> files) {
        return files.stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }
}
