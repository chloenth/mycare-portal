package com.mycareportal.pharmacy.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.pharmacy.dto.ApiResponse;
import com.mycareportal.pharmacy.dto.reponse.MedicineResponse;
import com.mycareportal.pharmacy.dto.request.MedicineCreationRequest;
import com.mycareportal.pharmacy.service.MedicineService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MedicineController {
	MedicineService medicineService;

	@PostMapping
	ApiResponse<MedicineResponse> addMedicine(MedicineCreationRequest request) {
		return ApiResponse.<MedicineResponse>builder().result(medicineService.addMedicine(request)).build();
	}

	@GetMapping
	ApiResponse<List<MedicineResponse>> getAllMedicines() {
		return ApiResponse.<List<MedicineResponse>>builder().result(medicineService.getAllMedicines()).build();
	}

	@GetMapping("/{medicineId}")
	ApiResponse<MedicineResponse> getMedicineById(@PathVariable String medicineId) {
		return ApiResponse.<MedicineResponse>builder().result(medicineService.getMedicineById(medicineId)).build();
	}

	@GetMapping("/{medicineName}")
	ApiResponse<List<MedicineResponse>> getMedicinesByName(@PathVariable String medicineName) {
		return ApiResponse.<List<MedicineResponse>>builder().result(medicineService.getMedicinesByName(medicineName))
				.build();
	}

	@GetMapping("/{batchNumber}")
	ApiResponse<List<MedicineResponse>> getMedicinesByBatchNumber(@PathVariable String batchNumber) {
		return ApiResponse.<List<MedicineResponse>>builder()
				.result(medicineService.getMedicinesByBatchNumber(batchNumber)).build();
	}
	
	@DeleteMapping("/{medicineId}")
	ApiResponse<Void> deleteMedicineById(@PathVariable String medicineId) {
		medicineService.deleteMedicineById(medicineId);
		return ApiResponse.<Void>builder().message("Medicine has been deleted").build();
	}
	
	@DeleteMapping("/{batchNumber}")
	ApiResponse<Void> deleteMedicineByBatchNumber(@PathVariable String batchNumber) {
		medicineService.deleteMedicineByBatchNumber(batchNumber);
		return ApiResponse.<Void>builder().message("Medicine has been deleted").build();
	}
}
