package com.mycareportal.pharmacy.dto.reponse;

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
public class PrescriptionDetailResponse {
	String prescriptionSummaryId;
	String medicalRecordId;
	
	String medicineId;
	String medicineName;
	String dosage;
	LocalDate startDate;
	Double treatmentDays;
	Long quantity;
	String unit;
	String status;
	String doctorNote;
}
