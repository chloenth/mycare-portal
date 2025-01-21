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
public class MedicineResponse {
	String id;
	String name;
	String description;
	Long quantityInStock;
	String unit;
	LocalDate importedDate;
	LocalDate expiredDate;
	String batchNumber;
}
