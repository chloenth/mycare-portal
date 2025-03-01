package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mycareportal.identity.dto.request.user.UserCreationRequest;
import com.mycareportal.identity.dto.request.user.PasswordUpdateRequest;
import com.mycareportal.identity.dto.request.userprofile.UserProfileRequest;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;
import com.mycareportal.identity.dto.response.pagedata.user.UserWithProfileResponse;
import com.mycareportal.identity.dto.response.user.UserResponse;
import com.mycareportal.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	User toUser(UserCreationRequest request);

	
	UserResponse toUserResponse(User user);
	
	@Mapping(source = "user.id", target = "id")
	UserWithProfileResponse toUserWithProfileResponse(User user, ProfileResponse profile);

	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "id", ignore = true)
	void updateUser(@MappingTarget User user, PasswordUpdateRequest request);
	
	@Mapping(source = "response.profile.id", target = "profileId")
	@Mapping(source = "response.profile.address", target = "address")
	@Mapping(source = "response.profile.dob", target = "dob")
	@Mapping(source = "response.profile.email", target = "email")
	@Mapping(source = "response.profile.fullName", target = "fullName")
	@Mapping(source = "response.profile.gender", target = "gender")
	@Mapping(source = "response.profile.phoneNumber", target = "phoneNumber")
	@Mapping(source = "response.profile.avatar", target = "avatar")
	UserProfileRequest toUserProfileRequest(UserWithProfileResponse response);
}
