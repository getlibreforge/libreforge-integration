package com.libreforge.integration.images.api.dto.response;

import com.libreforge.integration.images.api.service.dto.TagDTO;

import java.util.List;

public class ImageUploadResponse {
    private String id;
    private String url;
    private List<TagDTO> tagDTOs;

    public ImageUploadResponse(String id, String url, List<TagDTO> tagDTOs) {
        this.id = id;
        this.url = url;
        this.tagDTOs = tagDTOs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TagDTO> getTags() {
        return tagDTOs;
    }

    public void setTags(List<TagDTO> tagDTOs) {
        this.tagDTOs = tagDTOs;
    }
}
