package com.mycareportal.identity.exception;

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
	INVALID_DOB(1006, "Invalid date of birth. It must be before the current date", HttpStatus.BAD_REQUEST),
	INVALID_FILE_AVATAR(1007, "File size must be <= 100KB", HttpStatus.BAD_REQUEST),
	
	EMAIL_ALREADY_TAKEN(1008, "Email has already been registered", HttpStatus.BAD_REQUEST),
	UNAUTHENTICATED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
	INVALID_REFRESH_TOKEN(1011, "Invalid refresh token. Please log in again", HttpStatus.BAD_REQUEST),
	ROLE_NOT_FOUND(1012, "Role not found", HttpStatus.BAD_REQUEST),;

	ErrorCode(int code, String message, HttpStatusCode statusCode) {
		this.code = code;
		this.message = message;
		this.statusCode = statusCode;
	}

	private final int code;
	private final String message;
	private final HttpStatusCode statusCode;
}
