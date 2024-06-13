package com.libreforge.integration.designer.image.server.utils;

import com.libreforge.integration.designer.image.server.api.dto.ImageResponse;
import com.libreforge.integration.designer.image.server.common.FileExtension;
import com.libreforge.integration.designer.image.server.service.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public final class FileUtils {

    public static final List<String> ALLOWED_CONTENT_TYPES =
            List.of(
                FileExtension.JPG.getContentType(),
                FileExtension.PNG.getContentType(),
                FileExtension.SVG.getContentType()
            );

    private FileUtils() {
    }

    public static String buildUrl(String template, String bucketName, String id) {
        return String.format(template, bucketName, id);
    }

    public static ImageResponse convertToImageResponse(FileDTO fileDTO) {
        return new ImageResponse(fileDTO.getId(), fileDTO.getUrl(), fileDTO.getTags());
    }

    public static String getContentType(MultipartFile file) {
        return FileExtension.parse(file.getOriginalFilename(), null);
    }

    public static String getFileExtension(String originalFilename) {
        return FileExtension.parse(originalFilename, "");
    }

    public static String generateFileKey(String id, String extension) {
        return String.format("%s.%s", id, extension);
    }
}
