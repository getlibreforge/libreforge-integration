package com.libreforge.integration.designer.image.server.api.dto;

import java.util.List;

public class ImageResponse {
    private String id;
    private String url;
    private List<String> tags;

    public ImageResponse(String id, String url, List<String> tags) {
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
