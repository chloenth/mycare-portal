package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.identity.dto.request.DoctorCreationRequest;
import com.mycareportal.identity.dto.request.user.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
	@Mapping(target="userId", ignore = true)
	@Mapping(target="profileId", ignore = true)
	DoctorCreationRequest toDoctorCreationRequest(UserCreationRequest request);
}
