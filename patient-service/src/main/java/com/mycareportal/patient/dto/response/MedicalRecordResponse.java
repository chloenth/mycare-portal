package com.mycareportal.patient.dto.response;

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
public class MedicalRecordResponse {
	String id;
	String visitSessionId;
	String visitDate;
	String departmentId;
	String doctorId;
	String patientSymptoms;
	String doctorDiagnosis;
	String doctorNote;
}
