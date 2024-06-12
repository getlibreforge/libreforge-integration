package com.libreforge.integration.images.api.dto.request;

import com.libreforge.integration.images.api.service.dto.TagDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageUploadRequest {
    private MultipartFile image;
    private List<TagDTO> tagDTOS;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public List<TagDTO> getTags() {
        return tagDTOS;
    }

    public void setTags(List<TagDTO> tagDTOS) {
        this.tagDTOS = tagDTOS;
    }
}
