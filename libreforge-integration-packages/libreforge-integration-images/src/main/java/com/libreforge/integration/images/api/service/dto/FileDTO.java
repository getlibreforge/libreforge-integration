package com.libreforge.integration.images.api.service.dto;

import java.util.List;

public class FileDTO {
    private String id;
    private String url;
    private List<TagDTO> tagDTOS;

    public FileDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public FileDTO(String id, String url, List<TagDTO> tagDTOS) {
        this.id = id;
        this.url = url;
        this.tagDTOS = tagDTOS;
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
        return tagDTOS;
    }

    public void setTags(List<TagDTO> tagDTOS) {
        this.tagDTOS = tagDTOS;
    }
}
