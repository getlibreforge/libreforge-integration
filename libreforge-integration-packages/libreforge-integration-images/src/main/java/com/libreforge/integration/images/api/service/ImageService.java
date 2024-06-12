package com.libreforge.integration.images.api.service;

import com.libreforge.integration.images.api.service.dto.FileDTO;
import com.libreforge.integration.images.api.service.dto.TagDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    /**
     * Uploads a file to data store.
     *
     * @param file    The MultipartFile to be uploaded.
     * @param tagDTOs The list of tags associated with the file.
     * @return The FileDTO containing the ID and URL of the uploaded file.
     * @throws IOException If an I/O error occurs during file upload.
     */
    FileDTO uploadFile(MultipartFile file, List<TagDTO> tagDTOs) throws IOException;

    /**
     * Retrieves all files from data store.
     *
     * @return The list of FileDTOs representing the files in the bucket.
     */
    List<FileDTO> getAllFiles();

    /**
     * Deletes an image from data store.
     *
     * @param id The ID of the image to be deleted.
     * @return True if the image is deleted successfully, false otherwise.
     */
    boolean deleteImage(String id);
}
