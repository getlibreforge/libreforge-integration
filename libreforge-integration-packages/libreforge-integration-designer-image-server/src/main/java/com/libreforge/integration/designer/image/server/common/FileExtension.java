package com.libreforge.integration.designer.image.server.common;

public enum FileExtension {

    PNG("png", "image/png"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpeg"),
    SVG("svg", "image/svg+xml");

    private String contentType;
    private String extension;
    private String fileEnding;

    FileExtension(String extension, String contentType) {
        this.extension = extension;
        this.fileEnding = "." + extension;
        this.contentType = contentType;
    }

    private boolean isEndingOf(String filename) {
        return filename != null && filename.toUpperCase().endsWith(fileEnding.toUpperCase());
    }

    public static String parse(String filename, String defaultValue) {
        if (filename == null) {
            return null;
        }

        for (FileExtension ext: FileExtension.values()) {
            if (ext.isEndingOf(filename)) {
                return ext.getExtension();
            }
        }

        return defaultValue;
    }

    public String getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }
}
