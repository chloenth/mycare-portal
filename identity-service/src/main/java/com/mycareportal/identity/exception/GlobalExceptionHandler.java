package com.mycareportal.identity.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mycareportal.identity.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private static final String MIN_ATTRIBUTE = "min";

	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiResponse<String>> handlingAppException(AppException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		ApiResponse<String> apiResponse = new ApiResponse<>();

		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<String>> handlingValidation(MethodArgumentNotValidException exception) {
		FieldError fieldError = exception.getFieldError();
		String enumKey = (fieldError != null) ? fieldError.getDefaultMessage() : "UNCATEGORIZED_EXCEPTION";

		ErrorCode errorCode = ErrorCode.INVALID_KEY;
		Map<String, Object> attributes = new HashMap<>();

		try {
			errorCode = ErrorCode.valueOf(enumKey);
			var constraintViolation = exception.getBindingResult().getAllErrors().getFirst()
					.unwrap(ConstraintViolation.class);

			// to retrieve the attribute
			Map<?, ?> rawAttributes = constraintViolation.getConstraintDescriptor().getAttributes();

			for (Map.Entry<?, ?> entry : rawAttributes.entrySet()) {
				if (entry.getKey() instanceof String) {
					attributes.put((String) entry.getKey(), entry.getValue());
				}
			}

			log.info("aatributes: {}", attributes.toString());
		} catch (IllegalArgumentException e) {

		}

		ApiResponse<String> apiResponse = new ApiResponse<>();

		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(attributes.containsKey(MIN_ATTRIBUTE) ? mapAttribute(errorCode.getMessage(), attributes)
				: errorCode.getMessage());

		return ResponseEntity.badRequest().body(apiResponse);

	}

	private String mapAttribute(String message, Map<String, Object> attributes) {
		String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

		return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
	}
}
