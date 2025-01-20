package com.mycareportal.patient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.patient.dto.response.MedicalRecordResponse;
import com.mycareportal.patient.entity.MedicalRecord;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
	@Mapping(target = "visitSessionId", source = "visitSession.id")
	@Mapping(target = "visitDate", source = "visitSession.startDateTime")
	MedicalRecordResponse toMedicalRecordResponse(MedicalRecord medicalRecord);
}
