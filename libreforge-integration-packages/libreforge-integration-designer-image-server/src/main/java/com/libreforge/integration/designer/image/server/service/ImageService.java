package com.libreforge.integration.designer.image.server.service;

import com.libreforge.integration.common.ApplicationException;
import com.libreforge.integration.designer.image.server.service.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    /**
     * Uploads a file to data store.
     *
     * @param file    The MultipartFile to be uploaded.
     * @param tags The list of tags associated with the file.
     * @return The FileDTO containing the ID and URL of the uploaded file.
     * @throws IOException If an I/O error occurs during file upload.
     */
    FileDTO uploadFile(MultipartFile file, List<String> tags) throws IOException;

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
     */
    void deleteImage(String id) throws ApplicationException ;
}
