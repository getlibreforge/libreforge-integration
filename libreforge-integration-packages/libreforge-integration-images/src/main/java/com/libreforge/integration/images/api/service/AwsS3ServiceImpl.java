package com.libreforge.integration.images.api.service;

import com.libreforge.integration.images.api.service.dto.FileDTO;
import com.libreforge.integration.images.api.service.dto.TagDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.libreforge.integration.images.utils.Utils.*;

/**
 * Service class for interacting with AWS S3.
 */
@Service
public class AwsS3ServiceImpl implements ImageService {
    private static final Logger LOG = LoggerFactory.getLogger(AwsS3ServiceImpl.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public AwsS3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public FileDTO uploadFile(MultipartFile file, List<TagDTO> tagDTOs) throws IOException {
        String id = UUID.randomUUID().toString();

        Path tempFile = Files.createTempFile(null, null);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Upload file to S3
        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(id)
                .contentType(getContentType(file));

        if (tagDTOs != null && !tagDTOs.isEmpty()) {
            // Convert tags to S3 Tag format
            List<Tag> s3Tags = tagDTOs.stream()
                    .map(tag -> Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build())
                    .collect(Collectors.toList());

            builder.tagging(Tagging.builder().tagSet(s3Tags).build());
        }
        PutObjectRequest putRequest = builder.build();
        s3Client.putObject(putRequest, tempFile);
        Files.delete(tempFile);

        LOG.info("Image uploaded successfully. Id: {}", id);

        return new FileDTO(id, buildUrl(bucketName, id));
    }

    @Override
    public List<FileDTO> getAllFiles() {
        List<FileDTO> files = new ArrayList<>();
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
        List<S3Object> contents = listObjectsV2Response.contents();

        for (S3Object objectSummary : contents) {
            String fileName = objectSummary.key();
            String fileUrl = buildUrl(bucketName, fileName);
            List<TagDTO> tagDTOs = getFileTags(fileName);  // Fetch tags for each file

            FileDTO fileDTO = new FileDTO(fileName, fileUrl, tagDTOs);
            files.add(fileDTO);
        }
        LOG.info("Number of objects in the bucket: {}", contents.size());

        return files;
    }

    @Override
    public boolean deleteImage(String id) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(id).build();
        s3Client.deleteObject(deleteObjectRequest);

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucketName).key(id).build();
            s3Client.headObject(headObjectRequest);
            LOG.info("File with id {} still present", id);
            return false;
        } catch (NoSuchKeyException e) {
            LOG.info("File with id {} was deleted", id);
            return true;
        } catch (S3Exception e) {
            LOG.info("File with id {} still present. Error message: {}", id, e.getMessage());
            return false;
        }
    }

    private String getContentType(MultipartFile file) {
        String contentType = null;
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            if (fileName.endsWith(PNG_EXT)) {
                contentType = PNG_CONTENT_TYPE;
            } else if (fileName.endsWith(JPG_EXT) || fileName.endsWith(JPEG_EXT)) {
                contentType = JPEG_CONTENT_TYPE;
            } else if (fileName.endsWith(SVG_EXT)) {
                contentType = SVG_CONTENT_TYPE;
            }
        }

        return contentType;
    }

    private List<TagDTO> getFileTags(String fileName) {
        List<TagDTO> tagDTOs = new ArrayList<>();
        GetObjectTaggingRequest getTaggingRequest = GetObjectTaggingRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        GetObjectTaggingResponse getTaggingResponse = s3Client.getObjectTagging(getTaggingRequest);
        List<software.amazon.awssdk.services.s3.model.Tag> s3Tags = getTaggingResponse.tagSet();

        for (software.amazon.awssdk.services.s3.model.Tag s3Tag : s3Tags) {
            tagDTOs.add(new TagDTO(s3Tag.key(), s3Tag.value()));
        }

        return tagDTOs;
    }
}