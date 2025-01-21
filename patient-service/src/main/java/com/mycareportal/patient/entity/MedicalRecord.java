package com.mycareportal.patient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class MedicalRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;

	@ManyToOne
	@JoinColumn(name = "visit_session_id")
	VisitSession visitSession;

	String departmentId;
	String doctorId;
	String patientSymptoms;
	String doctorDiagnosis;
	String doctorNote;
}
