package com.mycareportal.pharmacy.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class MedicineCreationRequest {
	String name;
	String description;
	
	@NotNull
	@PositiveOrZero
	Long quantityInStock;
	
	String unit;
	LocalDate importedDate;
	
	@FutureOrPresent
	LocalDate expiredDate;
}
