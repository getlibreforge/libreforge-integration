package com.libreforge.integration.designer.image.server.service.impl;

import com.libreforge.integration.common.ApplicationException;
import com.libreforge.integration.designer.image.server.exception.ObjectNotFoundException;
import com.libreforge.integration.designer.image.server.service.ImageService;
import com.libreforge.integration.designer.image.server.service.dto.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.libreforge.integration.designer.image.server.utils.FileUtils.*;

@Service
public class AwsS3ServiceImpl implements ImageService {
    private static final Logger LOG = LoggerFactory.getLogger(AwsS3ServiceImpl.class);

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.public.url.template}")
    private String publicUrlTemplate;

    @Override
    public FileDTO uploadFile(MultipartFile file, List<String> tags) throws IOException {
        String fileKey = generateFileKey(UUID.randomUUID().toString(),
                getFileExtension(file.getOriginalFilename()));

        Path tempFile = Files.createTempFile(null, null);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(bucketName).key(fileKey)
                .contentType(getContentType(file));

        if (tags != null && !tags.isEmpty()) {
            List<Tag> s3Tags = tags.stream().map(value -> Tag.builder().key(value).value(value).build()).collect(Collectors.toList());

            builder.tagging(Tagging.builder().tagSet(s3Tags).build());
        }

        PutObjectRequest putRequest = builder.build();
        s3Client.putObject(putRequest, tempFile);
        Files.delete(tempFile);

        LOG.info("Image uploaded successfully. Key: {}", fileKey);
        return new FileDTO(fileKey, buildUrl(publicUrlTemplate, bucketName, fileKey));
    }

    @Override
    public List<FileDTO> getAllFiles() {

        List<FileDTO> response = new ArrayList<>();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(
            ListObjectsV2Request.builder().bucket(bucketName).build()
        );
        List<S3Object> contents = listObjectsV2Response.contents();

        for (S3Object obj : contents) {
            String fileKey = obj.key();
            String fileUrl = buildUrl(publicUrlTemplate, bucketName, fileKey);
            List<String> tags = getFileTags(fileKey);  // Fetch tags for each file

            response.add(new FileDTO(fileKey, fileUrl, tags));
        }

        return response;
    }

    @Override
    public void deleteImage(String fileKey) throws ApplicationException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName).key(fileKey).build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        }
    }

    private List<String> getFileTags(String fileName) {
        List<String> tags = new ArrayList<>();
        GetObjectTaggingRequest getTaggingRequest = GetObjectTaggingRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectTaggingResponse getTaggingResponse = s3Client.getObjectTagging(getTaggingRequest);
        List<Tag> s3Tags = getTaggingResponse.tagSet();

        for (Tag s3Tag : s3Tags) {
            tags.add(s3Tag.value());
        }

        return tags;
    }
}