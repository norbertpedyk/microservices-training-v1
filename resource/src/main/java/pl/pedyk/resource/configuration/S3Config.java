package pl.pedyk.resource.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class S3Config {

    @Value("${aws.s3.service-endpoint}")
    private String serviceEndpoint;
    @Value("${aws.s3.access-key}")
    private String accessKey;
    @Value("${aws.s3.secret-key}")
    private String secretKey;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    @Bean
    public MinioClient s3Client() throws InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException, ServerException {
        MinioClient minioClient = MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(serviceEndpoint)
                .build();
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
        return minioClient;
    }
}

