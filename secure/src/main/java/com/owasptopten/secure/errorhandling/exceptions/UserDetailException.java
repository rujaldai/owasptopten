package com.owasptopten.secure.errorhandling.exceptions;

import com.owasptopten.secure.errorhandling.enums.ErrorCode;
import com.owasptopten.secure.errorhandling.exceptions.ApplicationException;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class UserDetailException extends ApplicationException {

    private final ErrorCode errorCode;

    public UserDetailException(String message) {
        super(message);
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public static UserDetailException userAlreadyExists() {
        return new UserDetailException(ErrorCode.USER_ALREADY_EXISTS);
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public UserDetailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static UserDetailException notFound() {
        return new UserDetailException(ErrorCode.USER_NOT_FOUND);
    }

    @Override
    public String getCode() {
        return this.errorCode.getCode();
    }

    @Override
    public HttpStatusCode getHttpStatus() {
        return this.errorCode.getHttpStatus();
    }
}
