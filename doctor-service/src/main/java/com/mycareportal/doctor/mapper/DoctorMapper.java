package com.mycareportal.doctor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.doctor.dto.request.DoctorCreationRequest;
import com.mycareportal.doctor.dto.response.DoctorResponse;
import com.mycareportal.doctor.entity.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "department", ignore = true)
	Doctor toDoctor(DoctorCreationRequest request);
	
	@Mapping(target = "department", source = "department.name")
	DoctorResponse toDoctorResponse(Doctor doctor);
}
