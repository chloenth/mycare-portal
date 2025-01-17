package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;

import com.mycareportal.identity.dto.request.ProfileCreationRequest;
import com.mycareportal.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
