package com.libreforge.integration.images.api.dto.response;

import java.util.List;

public class ImageResponse {
    private String id;
    private String url;
    private List<TagResponse> tagDTOs;

    public ImageResponse(String id, String url, List<TagResponse> tagDTOs) {
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

    public List<TagResponse> getTagDTOs() {
        return tagDTOs;
    }

    public void setTagDTOs(List<TagResponse> tagDTOs) {
        this.tagDTOs = tagDTOs;
    }
}
