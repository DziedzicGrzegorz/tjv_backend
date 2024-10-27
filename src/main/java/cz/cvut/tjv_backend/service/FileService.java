package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.entity.File;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class FileService {

    private FileRepository fileRepository;
    private UserService userService;

    public Optional<File> getFileById(UUID fileId) {
        return fileRepository.findById(fileId);
    }

    public File saveFile(UUID ownerId,MultipartFile file) {
        User user = userService.getUserById(ownerId).orElseThrow(() -> new IllegalArgumentException("User not found"));

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

    public File updateFile(UUID founderId,UUID fileId, MultipartFile updatedFile) {
        userService.getUserById(founderId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));

        //TODO uploading new file

        file.setFilename(updatedFile.getOriginalFilename());
        file.setFileType(updatedFile.getContentType());
        file.setSize(updatedFile.getSize());
        file.setBlobUrl("files/" + updatedFile.getOriginalFilename());
        file.setUpdatedAt(LocalDateTime.now());
        file.setVersion(file.getVersion() + 1);

        return fileRepository.save(file);
    }

    public void deleteFile(UUID fileId) {
        fileRepository.deleteById(fileId);
    }

    public void deleteFiles(List<UUID> fileIds) {
        fileRepository.deleteAllById(fileIds);
    }

    public List<File> getAllFilesByUser(UUID userId) {
        return fileRepository.findAllFilesByUserId(userId);
    }

    public List<File> getFilesOwnedByUser(UUID userId) {
        return fileRepository.findFilesOwnedByUser(userId);
    }

    public Resource getFileAsResource(File file) {
        try {
            Path path = Paths.get(file.getBlobUrl());
            byte[] data = Files.readAllBytes(path);
            return new ByteArrayResource(data);
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found");
        }
    }
}

