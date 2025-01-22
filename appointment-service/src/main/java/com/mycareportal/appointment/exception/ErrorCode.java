package com.mycareportal.appointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1601, "Uncategorized error", HttpStatus.BAD_REQUEST),
    APPOINTMENT_NOT_FOUND(1602, "Appointment not found", HttpStatus.NOT_FOUND),
    PRESCRIPTION_SUMMARY_NOT_FOUND(1503, "Prescription summary not found", HttpStatus.NOT_FOUND),
    MEDICINE_NOT_FOUND(1604, "Medicine not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1605, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1606, "You do not have permission", HttpStatus.FORBIDDEN),
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
