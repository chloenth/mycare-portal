package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;

import com.mycareportal.identity.dto.request.profile.ProfileCreationRequest;
import com.mycareportal.identity.dto.request.user.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
