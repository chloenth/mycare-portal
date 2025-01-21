package com.mycareportal.pharmacy.entity;

import java.time.LocalDate;

import com.mycareportal.pharmacy.entity.idclass.PrescriptionDetailId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(PrescriptionDetailId.class)
public class PrescriptionDetail {
	@Id
	@ManyToOne
	@JoinColumn(name = "prescription_summary_id")
	PrescriptionSummary prescriptionSummary;
	
	@Id
	String medicalRecordId;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "medicine_id")
	Medicine medicine;

	String dosage;
	LocalDate startDate;
	Double treatmentDays;
	Long quantity;
	String unit;
	String status;
	String doctorNote;
}
