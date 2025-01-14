package com.mycareportal.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
	USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
	INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD(1005, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
	EMAIL_TAKEN(1006, "This email address has already been registered", HttpStatus.BAD_REQUEST),
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
