package com.mycareportal.profile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1101, "Uncategorized error", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_FOUND(1102, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1103, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1104, "You do not have permission", HttpStatus.FORBIDDEN),
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
