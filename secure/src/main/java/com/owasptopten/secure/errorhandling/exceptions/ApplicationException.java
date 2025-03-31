package com.owasptopten.secure.errorhandling.exceptions;

import org.springframework.http.HttpStatusCode;

public abstract class ApplicationException extends RuntimeException {

    protected ApplicationException(String message) {
        super(message);
    }

    public abstract String getMessage();

    public abstract String getCode();

    public abstract HttpStatusCode getHttpStatus();

}
