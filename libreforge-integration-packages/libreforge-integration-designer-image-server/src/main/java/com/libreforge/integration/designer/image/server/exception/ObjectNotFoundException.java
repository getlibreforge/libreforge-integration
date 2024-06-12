package com.libreforge.integration.designer.image.server.exception;

import com.libreforge.integration.common.ApplicationException;

public class ObjectNotFoundException extends ApplicationException {

    public ObjectNotFoundException(String message) {
        super(message, "error_file_not_found");
    }
}
