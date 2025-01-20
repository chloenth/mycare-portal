package com.mycareportal.identity.dto.response;

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
public class PatientResponse {
	String id;
	String userId;
	String profileId;
	String allergies;
	String chronicIllnesses;
	String familyMedicalHistory;
	String bloodType;
	Double height;
	Double weight;
	Boolean hasInsurance;
	LocalDate createdDate;
}
