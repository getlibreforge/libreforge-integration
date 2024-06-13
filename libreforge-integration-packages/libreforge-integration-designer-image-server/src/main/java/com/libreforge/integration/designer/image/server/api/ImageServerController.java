package com.libreforge.integration.designer.image.server.api;

import com.libreforge.integration.common.ApplicationException;
import com.libreforge.integration.designer.image.server.api.dto.GetImagesRequest;
import com.libreforge.integration.designer.image.server.api.dto.ImageResponse;
import com.libreforge.integration.designer.image.server.exception.ContentTypeNotSupportedException;
import com.libreforge.integration.designer.image.server.exception.ObjectUploadException;
import com.libreforge.integration.designer.image.server.exception.EmptyResourceUploadException;
import com.libreforge.integration.designer.image.server.service.ImageService;
import com.libreforge.integration.designer.image.server.service.dto.FileDTO;
import com.libreforge.integration.designer.image.server.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.libreforge.integration.designer.image.server.utils.FileUtils.*;

/**
 * REST controller for managing image uploads and retrievals from AWS S3.
 */
@RestController
@RequestMapping("/api/designer/image-server/images")
public class ImageServerController {
    private static final Logger LOG = LoggerFactory.getLogger(ImageServerController.class);

    @Autowired
    private ImageService s3Service;

    /**
     * Uploads image along with its associated tags to AWS S3.
     */
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ImageResponse uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("tags") List<String> tags) throws ApplicationException {

        LOG.info("POST /images called");

        if (image == null) {
            throw new EmptyResourceUploadException("File can't be null");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(image.getContentType())) {
            throw new ContentTypeNotSupportedException("Content Type not supported");
        }

        return uploadSingleImage(image, tags);
    }

    /**
     * Retrieves images from AWS S3 that match the provided tag parameters
     */
    @PostMapping(value = "/search", produces = "application/json")
    public List<ImageResponse> getImages(@RequestBody GetImagesRequest body) {

        LOG.info("GET /images called with Tag params: {}", body.getTags());

        List<FileDTO> s3Images = s3Service.getAllFiles();
        Stream<FileDTO> filesStream = s3Images.stream();

        if (body.getTags() != null && !body.getTags().isEmpty()) {
            filesStream = filterImagesByTags(filesStream, body.getTags());
        }

        return filesStream.map(FileUtils::convertToImageResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deletes an image from AWS S3 by its ID
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public void deleteImage(@PathVariable("id") String id) throws ApplicationException {

        LOG.info("DELETE /images called for id = {}", id);
        s3Service.deleteImage(id);
    }

    private ImageResponse uploadSingleImage(MultipartFile image, List<String> tags)
            throws ApplicationException {

        try {
            FileDTO fileDTO = s3Service.uploadFile(image, tags);
            return new ImageResponse(fileDTO.getId(), fileDTO.getUrl(), tags);
        } catch (IOException e) {
            throw new ObjectUploadException(e.getMessage());
        }
    }

    private Stream<FileDTO> filterImagesByTags(Stream<FileDTO> filesStream, List<String> tags) {
        return filesStream.filter(s3Image -> {
            List<String> imageTags = s3Image.getTags();
            return tags.stream().anyMatch(imageTags::contains);
        });
    }
}
