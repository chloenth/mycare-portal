package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.identity.dto.request.PatientCreationRequest;
import com.mycareportal.identity.dto.request.user.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface PatientMapper {
	@Mapping(target="userId", ignore = true)
	@Mapping(target="profileId", ignore = true)
	PatientCreationRequest toPatientCreationRequest(UserCreationRequest request);
}
