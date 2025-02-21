package com.mycareportal.identity.dto.request.profile;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
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
public class ProfileCreationRequest {
	@NotEmpty
	Long userId;
	String email;
	String fullName;
	LocalDate dob;
	String gender;
	String address;
	String phoneNumber;
}
