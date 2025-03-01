package com.mycareportal.identity.dto.request.userprofile;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mycareportal.identity.dto.response.role.RoleResponse;

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
public class UserProfileRequest {
	Long id;

	String username;
	Set<RoleResponse> roles;
	
	Long profileId;
	String fullName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	LocalDate dob;
	
	String gender;
	String avatar;
	String email;
	String phoneNumber;
	String address;
}
