package com.mycareportal.pharmacy.dto.request;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionDetailCreationRequest {
	String prescriptionSummaryId;
	String medicalRecordId;
	String medicineId;
	
	
	String dosage;
	LocalDate startDate;
	Double treatmentDays;
	Long quantity;
	String unit;
	String status;
	String doctorNote;
}
