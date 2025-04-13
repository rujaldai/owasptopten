package com.owasptopten.secure.errorhandling.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
public enum ErrorCode {

    USER_NOT_FOUND("U404", "User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("U401", "Invalid credentials", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("U403", "Invalid token", HttpStatus.FORBIDDEN),
    INVALID_REQUEST("U400", "Invalid request", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS("U401", "User already exists", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("U500", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("U401", "Unauthorized", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatusCode httpStatus;
}
