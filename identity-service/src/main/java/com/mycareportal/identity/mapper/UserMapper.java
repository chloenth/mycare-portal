package com.mycareportal.identity.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mycareportal.identity.dto.request.user.UserCreationRequest;
import com.mycareportal.identity.dto.request.user.UserUpdateRequest;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;
import com.mycareportal.identity.dto.response.pagedata.user.PageDataUserResponse;
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
	@Mapping(target = "username", ignore = true)
	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
