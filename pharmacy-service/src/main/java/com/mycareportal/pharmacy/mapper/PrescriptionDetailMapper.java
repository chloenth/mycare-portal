package com.mycareportal.pharmacy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.pharmacy.dto.reponse.PrescriptionDetailResponse;
import com.mycareportal.pharmacy.dto.request.PrescriptionDetailCreationRequest;
import com.mycareportal.pharmacy.entity.PrescriptionDetail;

@Mapper(componentModel = "spring")
public interface PrescriptionDetailMapper {
	@Mapping(target="medicine", ignore = true)
	@Mapping(target="prescriptionSummary", ignore = true)
	PrescriptionDetail toPrescriptionDetail(PrescriptionDetailCreationRequest request);
	
	@Mapping(target="medicineId", source="medicine.id")
	@Mapping(target="medicineName", source="medicine.name")
	@Mapping(target="prescriptionSummaryId", source="prescriptionSummary.id")
	PrescriptionDetailResponse toPrescriptionDetailResponse(PrescriptionDetail prescriptionDetail);
}
