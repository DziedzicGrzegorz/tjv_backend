package cz.cvut.tjv_backend.storage.interfaces;

import cz.cvut.tjv_backend.exception.Exceptions.StorageDeleteException;
import cz.cvut.tjv_backend.exception.Exceptions.StorageDownloadException;
import cz.cvut.tjv_backend.exception.Exceptions.StorageUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface StorageService {
    void uploadFile(String path, MultipartFile file) throws StorageUploadException;

    void deleteFile(String path) throws StorageDeleteException;

    void downloadFile(String path, OutputStream outputStream) throws StorageDownloadException;
}
