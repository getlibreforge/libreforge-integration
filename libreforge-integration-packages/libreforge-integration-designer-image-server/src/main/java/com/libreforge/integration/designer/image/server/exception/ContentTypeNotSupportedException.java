package com.libreforge.integration.designer.image.server.exception;

import com.libreforge.integration.common.ApplicationException;

public class ContentTypeNotSupportedException extends ApplicationException {

    public ContentTypeNotSupportedException(String message) {
        super(message, "error_not_allowed_content_type");
    }
}
