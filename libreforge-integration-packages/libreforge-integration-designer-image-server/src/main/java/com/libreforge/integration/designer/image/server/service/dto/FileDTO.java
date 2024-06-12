package com.libreforge.integration.designer.image.server.service.dto;

import java.util.List;

public class FileDTO {
    private String id;
    private String url;
    private List<String> tags;

    public FileDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public FileDTO(String id, String url, List<String> tags) {
        this.id = id;
        this.url = url;
        this.tags = tags;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
