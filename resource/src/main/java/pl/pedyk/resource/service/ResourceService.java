package pl.pedyk.resource.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.pedyk.resource.model.ResourceTracking;
import pl.pedyk.resource.repository.ResourceTrackingRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ResourceService {

    private final MinioClient s3Client;
    private final ResourceTrackingRepository resourceTrackingRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public ResourceService(MinioClient s3client, ResourceTrackingRepository resourceTrackingRepository) {
        this.s3Client = s3client;
        this.resourceTrackingRepository = resourceTrackingRepository;
    }

    @Transactional
    public Map<String, Long> saveResource(byte[] audioData) throws InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException, ServerException {
        ResourceTracking resourceTracking = resourceTrackingRepository.save(new ResourceTracking(bucketName));
        InputStream inputStream = new ByteArrayInputStream(audioData);
        Long id = resourceTracking.getId();
        s3Client.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(id.toString())
                .stream(inputStream, inputStream.available(), -1)
                .contentType("application/octet-stream")
                .build());
        return new HashMap<>() {
            {
                put("id", id);
            }
        };
    }

    @Transactional
    public byte[] getResource(Long id) throws IOException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException, ServerException {
        return s3Client.getObject(
                GetObjectArgs.builder()
                        .bucket("mybucket")
                        .object(id.toString())
                        .build()).readAllBytes();
    }

    @Transactional
    public Map<String, long[]> deleteResources(String ids) {
        List<Result<DeleteError>> deletionErrors = deleteFromBucket(ids);

        if (deletionErrors.isEmpty()) {
            resourceTrackingRepository.deleteAllById(Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList()));
            return new HashMap<>() {{

                put("id", Arrays.stream(ids.split(","))
                        .mapToLong(Long::parseLong)
                        .toArray());
            }};
        } else {
            List<String> unableToDelete = findUnableToDelete(deletionErrors);
            List<Long> ableToDelete = findAbleToDelete(ids, unableToDelete);

            resourceTrackingRepository.deleteAllById(ableToDelete);
            return new HashMap<>() {{

                put("id", ableToDelete.stream()
                        .mapToLong(l -> l)
                        .toArray()
                );
            }};
        }
    }


    private List<Result<DeleteError>> deleteFromBucket(String ids) {
        return StreamSupport.stream(s3Client
                        .removeObjects(RemoveObjectsArgs.builder()
                                .bucket(bucketName)
                                .objects(Arrays.stream(ids.split(","))
                                        .map(DeleteObject::new)
                                        .collect(Collectors.toList()))
                                .build())
                        .spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<String> findUnableToDelete(List<Result<DeleteError>> deletionErrors) {
        return deletionErrors.stream()
                .map(deleteErrorResult -> {
                    try {
                        return deleteErrorResult.get().objectName();
                    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .toList();
    }

    private List<Long> findAbleToDelete(String ids, List<String> unableToDelete) {
        return Arrays.stream(ids.split(",")).toList().stream()
                .filter(id -> !unableToDelete.contains(id))
                .map(Long::parseLong).toList();
    }
}


