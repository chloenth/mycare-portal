package com.mycareportal.identity.dto.response.pagedata.user;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;
import com.mycareportal.identity.entity.Role;

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
public class UserWithProfileResponse {
	Long id;
	String username;
	ProfileResponse profile;
	Set<Role> roles;
}
