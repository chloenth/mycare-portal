package com.mycareportal.identity.dto.request.user;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycareportal.identity.validator.dob.DobConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreationRequest {
	@Size(min = 4, message = "INVALID_USERNAME")
	String username;

	@Size(min = 6, message = "INVALID_PASSWORD")
	String password;

	@Email(message = "INVALID_EMAIL")
	String email;

	String fullName;

	@DobConstraint(message = "INVALID_DOB")
	LocalDate dob;
	String gender;
	String address;
	String phoneNumber;
}
