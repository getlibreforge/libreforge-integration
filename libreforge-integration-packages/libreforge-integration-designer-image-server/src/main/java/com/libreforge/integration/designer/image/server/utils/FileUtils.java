package com.libreforge.integration.designer.image.server.utils;

import com.libreforge.integration.designer.image.server.api.dto.ImageResponse;
import com.libreforge.integration.designer.image.server.service.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public final class FileUtils {

    public static final String PNG_EXT = ".png";
    public static final String JPEG_EXT = ".jpeg";
    public static final String JPG_EXT = ".jpg";
    public static final String SVG_EXT = ".svg";
    public static final String JPEG_CONTENT_TYPE = "image/jpeg";
    public static final String PNG_CONTENT_TYPE = "image/png";
    public static final String SVG_CONTENT_TYPE = "image/svg+xml";

    public static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            JPEG_CONTENT_TYPE, PNG_CONTENT_TYPE, SVG_CONTENT_TYPE);

    private FileUtils() {
    }

    public static String buildUrl(String template, String bucketName, String id) {
        return String.format(template, bucketName, id);
    }

    public static ImageResponse convertToImageResponse(FileDTO fileDTO) {
        return new ImageResponse(fileDTO.getId(), fileDTO.getUrl(), fileDTO.getTags());
    }

    public static String getContentType(MultipartFile file) {
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
}
