package com.mycareportal.search.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mycareportal.search.dto.request.ProfileUpdateRequest;
import com.mycareportal.search.dto.request.UserProfileRequest;
import com.mycareportal.search.dto.response.profile.ProfileResponse;
import com.mycareportal.search.dto.response.user.UserResponse;
import com.mycareportal.search.dto.response.userprofile.UserProfileResponse;
import com.mycareportal.search.entity.UserProfileIndex;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
	@Mapping(target="id", source="user.id")
	@Mapping(target="profileId", source="profile.id")
	@Mapping(target="dob", ignore = true)
	UserProfileIndex toUserProfileIndex(UserResponse user, ProfileResponse profile);
	
	@Mapping(target="dob", ignore = true)
	UserProfileIndex toUserProfileIndex(UserProfileRequest request);

	@Mapping(target="dob", ignore = true)
	UserProfileResponse toUserProfileResponse(UserProfileIndex index);
	
	@Mapping(target="dob", ignore = true)
	@Mapping(target="profileId", ignore = true)
	@Mapping(target="roles", ignore = true)
	@Mapping(target="username", ignore = true)
	UserProfileIndex toUserProfileUpdateIndex(@MappingTarget UserProfileIndex index, ProfileUpdateRequest request);
}
