package com.mycareportal.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1301, "Uncategorized error", HttpStatus.BAD_REQUEST),
    PATIENT_NOT_FOUND(1302, "Patient not found", HttpStatus.NOT_FOUND),
    DEPARTMENT_NOT_FOUND(1303, "Department not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1304, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1305, "You do not have permission", HttpStatus.FORBIDDEN),
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
