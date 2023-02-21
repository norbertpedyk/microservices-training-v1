package pl.pedyk.resource.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.pedyk.resource.model.ResourceTracking;
import pl.pedyk.resource.repository.ResourceTrackingRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final AmazonS3 s3Client;
    private final ResourceTrackingRepository resourceTrackingRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public ResourceService(AmazonS3 s3client, ResourceTrackingRepository resourceTrackingRepository) {
        this.s3Client = s3client;
        this.resourceTrackingRepository = resourceTrackingRepository;
    }

    public Map<String, Long> saveResource(byte[] audioData) {
        ResourceTracking resourceTracking = resourceTrackingRepository.save(new ResourceTracking(bucketName));
        InputStream inputStream = new ByteArrayInputStream(audioData);
        Long id = resourceTracking.getId();
        s3Client.putObject(bucketName, id.toString(), inputStream, null);
        return new HashMap<>() {
            {
                put("id", id);
            }
        };
    }

    public byte[] getResource(Long id) throws IOException {
        return s3Client.getObject(bucketName, String.valueOf(id)).getObjectContent().readAllBytes();
    }

    public Map<String, long[]> deleteResources(String ids) {
        List<String> deletedObjectsIds = s3Client
                .deleteObjects(new DeleteObjectsRequest(bucketName)
                        .withKeys(ids))
                .getDeletedObjects().stream()
                .map(DeleteObjectsResult.DeletedObject::getKey).toList();

        resourceTrackingRepository.deleteAllById(deletedObjectsIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
        return new HashMap<>() {{

            put("id", deletedObjectsIds.stream()
                    .mapToLong(Long::parseLong)
                    .toArray());
        }};
    }

    private boolean isMp3Audio(byte[] data) throws IOException {
        if (data == null || data.length < 3) {
            return false;
        }
        if (data[0] != (byte) 0xFF || (data[1] & 0xE0) != 0xE0) {
            return false;
        }

        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(data));
        return mimeType != null && mimeType.equals("audio/mpeg");
    }
}


