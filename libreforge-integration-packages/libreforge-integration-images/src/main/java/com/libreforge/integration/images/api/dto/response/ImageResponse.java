package com.libreforge.integration.images.api.dto.response;

import java.util.List;

public class ImageResponse {
    private String id;
    private String url;
    private List<TagResponse> tags;

    public ImageResponse(String id, String url, List<TagResponse> tags) {
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

    public List<TagResponse> getTags() {
        return tags;
    }

    public void setTags(List<TagResponse> tags) {
        this.tags = tags;
    }
}
