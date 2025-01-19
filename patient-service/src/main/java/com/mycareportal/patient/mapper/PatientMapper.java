package com.mycareportal.patient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.patient.dto.request.PatientCreationRequest;
import com.mycareportal.patient.dto.response.PatientResponse;
import com.mycareportal.patient.entity.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	Patient toPatient(PatientCreationRequest request);
	
	PatientResponse toPatientResponse(Patient patient);
}
