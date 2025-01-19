package com.mycareportal.patient.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
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
