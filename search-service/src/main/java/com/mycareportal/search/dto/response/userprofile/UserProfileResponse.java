package com.mycareportal.search.dto.response.userprofile;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {
	Long id;
	String username;
	Set<RoleResponse> roles;
	
	Long profileId;
	String fullName;
	String dob;
	String gender;
	String avatar;
	String email;
	String phoneNumber;
	String address;
}
