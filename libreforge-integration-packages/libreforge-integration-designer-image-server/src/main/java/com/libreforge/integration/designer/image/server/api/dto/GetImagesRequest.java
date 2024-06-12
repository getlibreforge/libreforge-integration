package com.libreforge.integration.designer.image.server.api.dto;

import java.util.List;

public class GetImagesRequest {
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
