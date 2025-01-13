package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.identity.dto.request.UserCreationRequest;
import com.mycareportal.identity.dto.response.UserResponse;
import com.mycareportal.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	User toUser(UserCreationRequest request);

	UserResponse toUserResponse(User user);
}
