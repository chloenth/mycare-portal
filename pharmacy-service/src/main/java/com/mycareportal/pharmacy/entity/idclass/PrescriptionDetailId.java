package com.mycareportal.pharmacy.entity.idclass;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionDetailId implements Serializable {
    private static final long serialVersionUID = 1L; 
    
	String prescriptionSummary;
	String medicalRecordId;
	String medicine;
}
