package com.mycareportal.pharmacy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.pharmacy.dto.reponse.MedicineResponse;
import com.mycareportal.pharmacy.dto.request.MedicineCreationRequest;
import com.mycareportal.pharmacy.entity.Medicine;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "batchNumber", ignore = true)
	Medicine toMedicine(MedicineCreationRequest request);
	
	MedicineResponse toPharmacyResponse(Medicine medicine);
}
