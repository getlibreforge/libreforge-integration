package com.libreforge.integration.images.api;

import com.libreforge.integration.images.api.dto.response.ImageResponse;
import com.libreforge.integration.images.api.dto.response.ImageUploadResponse;
import com.libreforge.integration.images.api.service.ImageService;
import com.libreforge.integration.images.api.service.dto.FileDTO;
import com.libreforge.integration.images.api.service.dto.TagDTO;
import com.libreforge.integration.images.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.libreforge.integration.images.utils.Utils.*;

/**
 * REST controller for managing image uploads and retrievals from AWS S3.
 */
@RestController
@RequestMapping("/api/images")
public class ImagesController {
    private static final Logger LOG = LoggerFactory.getLogger(ImagesController.class);

    private final ImageService s3Service;

    public ImagesController(ImageService s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Uploads multiple images along with their associated tags to AWS S3.
     * Please check CURL request in README.md
     *
     * @param images List of images to be uploaded.
     * @param tagMap Map of image filenames to their associated tags.
     * @return A list of responses containing the URL and ID of each uploaded image along with their tags.
     */
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<List<ImageUploadResponse>> uploadImages(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam Map<String, String> tagMap) {

        LOG.info("POST /api/images called with {} images", images.size());

        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        for (MultipartFile image : images) {
            if (!ALLOWED_CONTENT_TYPES.contains(image.getContentType())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        Map<String, List<TagDTO>> imageTags = convertTagMap(tagMap);

        List<ImageUploadResponse> imageResponses = images.stream()
                .map(image -> uploadSingleImage(image, imageTags.get(image.getOriginalFilename())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(imageResponses);
    }

    /**
     * Retrieves images from AWS S3 that match the provided tag parameters.
     * Please check CURL request in README.md
     *
     * @param tagParams The tag parameters to filter images by.
     * @return A list of ImageResponse objects containing the images and their associated tags.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ImageResponse>> getImages(@RequestParam Map<String, String> tagParams) {
        LOG.info("GET /api/images called with Tag params: {}", tagParams);

        List<FileDTO> s3Images = s3Service.getAllFiles();
        Stream<FileDTO> filesStream = s3Images.stream();

        if (!tagParams.isEmpty()) {
            filesStream = filterImagesByTags(filesStream, tagParams);
        }

        List<ImageResponse> images = filesStream.map(Utils::convertToImageResponse).collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }

    /**
     * Deletes an image from AWS S3 by its ID.
     * Please check CURL request in README.md
     *
     * @param id The ID of the image to be deleted.
     * @return A response indicating the result of the delete operation.
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") String id) {
        LOG.info("DELETE /api/images called for id = {}", id);

        boolean isDeleted = s3Service.deleteImage(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private ImageUploadResponse uploadSingleImage(MultipartFile image, List<TagDTO> tagDTOList) {
        String fileName = image.getOriginalFilename();
        try {
            FileDTO fileDTO = s3Service.uploadFile(image, tagDTOList);
            return new ImageUploadResponse(fileDTO.getId(), fileDTO.getUrl(), tagDTOList);
        } catch (IOException e) {
            LOG.error("Failed to upload file {} to S3", fileName, e);
            return null;
        }
    }

    private Map<String, List<TagDTO>> convertTagMap(Map<String, String> tagMap) {
        Map<String, List<TagDTO>> imageTags = new HashMap<>();
        tagMap.forEach((fileName, tagString) -> {
            List<TagDTO> tagDTOList = Arrays.stream(tagString.split(TAG_SEPARATOR))
                    .map(tag -> {
                        String[] kv = tag.split(KEY_VALUE_SEPARATOR);
                        return new TagDTO(kv[0], kv[1]);
                    })
                    .collect(Collectors.toList());
            imageTags.put(fileName, tagDTOList);
        });
        return imageTags;
    }

    private Stream<FileDTO> filterImagesByTags(Stream<FileDTO> filesStream, Map<String, String> tagParams) {
        return filesStream.filter(s3Image -> {
            Map<String, String> imageTags = s3Image.getTags().stream()
                    .collect(Collectors.toMap(TagDTO::getKey, TagDTO::getValue));
            return tagParams.entrySet().stream()
                    .anyMatch(param -> param.getValue().equals(imageTags.get(param.getKey())));
        });
    }
}
