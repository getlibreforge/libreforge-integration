package com.libreforge.integration.designer.image.server.exception;

import com.libreforge.integration.common.ApplicationException;

public class ObjectUploadException extends ApplicationException {

    public ObjectUploadException(String message) {
        super(message, "error_file_upload");
    }
}
