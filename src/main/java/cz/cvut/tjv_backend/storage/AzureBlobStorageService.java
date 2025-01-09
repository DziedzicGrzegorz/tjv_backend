package cz.cvut.tjv_backend.storage;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobRequestConditions;
import cz.cvut.tjv_backend.exception.Exceptions.StorageDeleteException;
import cz.cvut.tjv_backend.exception.Exceptions.StorageDownloadException;
import cz.cvut.tjv_backend.exception.Exceptions.StorageUploadException;
import cz.cvut.tjv_backend.storage.interfaces.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class AzureBlobStorageService implements StorageService {

    private final BlobContainerClient blobContainerClient;

    @Override
    public void uploadFile(String path, MultipartFile file) throws StorageUploadException {
        // Metadata to add to the blob
        Map<String, String> metadata = Collections.singletonMap("uploadedBy", "123");

        // Context and timeout
        Context context = new Context("Key", "Value");
        var timeout = Duration.ofSeconds(30);

        try {
            // Get the blob client for the specific file path
            BlobClient blobClient = blobContainerClient.getBlobClient(path);

            // Upload the file
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            // Set metadata on the uploaded blob
            System.out.printf("Set metadata completed with status %d%n",
                    blobClient.setMetadataWithResponse(metadata, null, timeout, context).getStatusCode());

        } catch (IOException e) {
            throw new StorageUploadException("Error uploading file to Azure Blob Storage");
        } catch (UnsupportedOperationException e) {
            throw new StorageUploadException("Unsupported access condition for setting metadata");
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
            blobClient.getProperties();
            blobClient.downloadStream(outputStream);
        } catch (Exception e) {
            throw new StorageDownloadException("Error downloading file from Azure Blob Storage");
        }
    }
}


