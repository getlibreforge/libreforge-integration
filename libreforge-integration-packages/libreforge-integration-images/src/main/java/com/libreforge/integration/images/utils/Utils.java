package com.libreforge.integration.images.utils;

import com.libreforge.integration.images.api.dto.response.ImageResponse;
import com.libreforge.integration.images.api.dto.response.TagResponse;
import com.libreforge.integration.images.api.service.dto.FileDTO;
import com.libreforge.integration.images.api.service.dto.TagDTO;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static final String HTTPS = "https://";
    public static final String AWS_PATH = ".s3.amazonaws.com/";
    public static final String PNG_EXT = ".png";
    public static final String JPEG_EXT = ".jpeg";
    public static final String JPG_EXT = ".jpg";
    public static final String SVG_EXT = ".svg";
    public static final String JPEG_CONTENT_TYPE = "image/jpeg";
    public static final String PNG_CONTENT_TYPE = "image/png";
    public static final String SVG_CONTENT_TYPE = "image/svg+xml";
    public static final String TAG_SEPARATOR = ";";
    public static final String KEY_VALUE_SEPARATOR = ":";

    public static final List<String> ALLOWED_CONTENT_TYPES = List.of(JPEG_CONTENT_TYPE, PNG_CONTENT_TYPE, SVG_CONTENT_TYPE);

    private Utils() {
    }

    public static String buildUrl(String bucketName, String id) {
        return HTTPS + bucketName + AWS_PATH + id;
    }

    public static List<TagResponse> convertToTagResponse(List<TagDTO> tagDTOs) {
        return tagDTOs.stream()
                .map(tagDTO -> new TagResponse(tagDTO.getKey(), tagDTO.getValue()))
                .collect(Collectors.toList());
    }

    public static ImageResponse convertToImageResponse(FileDTO fileDTO) {
        return new ImageResponse(fileDTO.getId(), fileDTO.getUrl(), convertToTagResponse(fileDTO.getTags()));
    }
}
