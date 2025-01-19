package com.mycareportal.patient.dto.response;

import java.time.LocalDate;
import java.util.List;

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
public class PatientResponse {
	String id;
	String userId;
	String profileId;
	List<String> allergies;
	List<String> chronicIllnesses;
	List<String> familyMedicalHistory;
	String bloodType;
	String height;
	String weight;
	Boolean hasInsurance;
	LocalDate createdDate;
}
