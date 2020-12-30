package com.codmain.orderapi.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidateServiceException extends RuntimeException {
    public ValidateServiceException() {
        super();
    }

    public ValidateServiceException(String message, Throwable cause, boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }

    public ValidateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateServiceException(String message) {
        super(message);
    }

    public ValidateServiceException(Throwable cause) {
        super(cause);
    }
}
