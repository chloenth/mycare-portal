package com.mycareportal.identity.dto.response.authentication;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycareportal.identity.dto.response.role.RoleResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
	@JsonIgnore
	String accessToken;
	
	@JsonIgnore
	String refreshToken;
	
	Set<RoleResponse> roles;
}
