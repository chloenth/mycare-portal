package com.mycareportal.profile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTED(1002, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND),
    INVALID_USERNAME(1004, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_TAKEN(1006, "Email has already been registered", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_REFRESH_TOKEN(1009, "Invalid refresh token. Please log in again", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
