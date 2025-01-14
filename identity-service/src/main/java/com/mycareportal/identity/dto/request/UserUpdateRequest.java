package com.mycareportal.identity.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.mycareportal.identity.validator.DobConstraint;

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
public class UserUpdateRequest {
	@Size(min = 6, message = "INVALID_PASSWORD")
	String password;

	@Email(message = "INVALID_EMAIL")
	String email;

	@DobConstraint(message = "INVALID_DOB")
	LocalDate dob;
	String gender;
	String address;
	String phoneNumber;
	List<String> roles;
}
