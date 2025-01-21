package com.mycareportal.pharmacy.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class InvalidatedMedicine {
	@Id
	String medicineId;
	
	@Column(nullable = false)
	String name;
	String description;
	Long quantityInStock;
	String unit;
	LocalDate importedDate;
	LocalDate expiredDate;
	String batchNumber;
	String reason;
}
