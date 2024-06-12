package com.libreforge.integration.designer.image.server.exception;

import com.libreforge.integration.common.ApplicationException;

public class EmptyResourceUploadException extends ApplicationException {

    public EmptyResourceUploadException(String message) {
        super(message, "error_file_upload_no_file");
    }
}
