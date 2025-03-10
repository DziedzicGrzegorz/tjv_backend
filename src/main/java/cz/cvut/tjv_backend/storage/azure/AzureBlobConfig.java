package cz.cvut.tjv_backend.storage.azure;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobConfig {

    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Value("${azure.storage.container.name}")
    private String containerName;

    @Value("${azure.storage.queue.name}")
    private String queueName;

    @Bean
    public BlobServiceClient clobServiceClient() {

        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

    }

    @Bean
    public BlobContainerClient blobContainerClient() {

        return clobServiceClient()
                .getBlobContainerClient(containerName);
    }

    @Bean
    public QueueClient queueClient() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .buildClient();

    }
}
