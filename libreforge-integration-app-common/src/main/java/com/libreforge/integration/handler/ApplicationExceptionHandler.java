package com.libreforge.integration.handler;

import com.libreforge.integration.common.ApplicationException;
import com.libreforge.integration.common.BusinessRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleApplicationException(ApplicationException e) {
        LOG.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .header("Access-Control-Allow-Origin", "*")
            .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<?> handleBusinessRuleException(BusinessRuleException e) {
        LOG.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .header("Access-Control-Allow-Origin", "*")
            .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }
}
