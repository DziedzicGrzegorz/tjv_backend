package cz.cvut.tjv_backend.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import cz.cvut.tjv_backend.exception.Exceptions.StorageDeleteException;
import cz.cvut.tjv_backend.exception.Exceptions.StorageDownloadException;
import cz.cvut.tjv_backend.exception.Exceptions.StorageUploadException;
import cz.cvut.tjv_backend.storage.interfaces.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
@Service
public class AzureBlobStorageService implements StorageService {

    private final BlobContainerClient blobContainerClient;

    @Override
    public void uploadFile(String path, MultipartFile file) throws StorageUploadException {
        try {
            BlobClient blobClient = blobContainerClient.getBlobClient(path);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
        } catch (IOException e) {
            throw new StorageUploadException("Error uploading file to Azure Blob Storage");
        }
    }

    @Override
    public void deleteFile(String path) throws StorageDeleteException {
        try {
            BlobClient blobClient = blobContainerClient.getBlobClient(path);
            blobClient.delete();
        } catch (Exception e) {
            throw new StorageDeleteException("Error deleting file from Azure Blob Storage");
        }
    }

    @Override
    public void downloadFile(String path, OutputStream outputStream) throws StorageDownloadException {
        try {
            BlobClient blobClient = blobContainerClient.getBlobClient(path);
            blobClient.downloadStream(outputStream);
        } catch (Exception e) {
            throw new StorageDownloadException("Error downloading file from Azure Blob Storage");
        }
    }
}


