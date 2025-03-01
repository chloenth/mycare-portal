package com.mycareportal.search.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.mycareportal.search.dto.response.role.RoleResponse;

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
	LocalDate dob;
	String gender;
	String email;
	String phoneNumber;
	String address;
	String avatar;
}
