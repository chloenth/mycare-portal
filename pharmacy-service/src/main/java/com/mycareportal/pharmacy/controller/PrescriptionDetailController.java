package com.mycareportal.pharmacy.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.pharmacy.dto.ApiResponse;
import com.mycareportal.pharmacy.dto.reponse.PrescriptionDetailResponse;
import com.mycareportal.pharmacy.dto.request.PrescriptionDetailCreationRequest;
import com.mycareportal.pharmacy.service.PrescriptionDetailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PrescriptionDetailController {
	PrescriptionDetailService prescriptionDetailService;
	
	@PostMapping
	ApiResponse<PrescriptionDetailResponse> createPrescriptionDetail(@RequestBody PrescriptionDetailCreationRequest request) {
		return ApiResponse.<PrescriptionDetailResponse>builder()
				.result(prescriptionDetailService.createPrescriptionDetail(request)).build();
	}
	
	@GetMapping("/{summaryId}")
	ApiResponse<List<PrescriptionDetailResponse>> getPrescriptionDetailBySummaryId(@PathVariable String summaryId) {
		return ApiResponse.<List<PrescriptionDetailResponse>>builder()
				.result(prescriptionDetailService.getDetailBySummaryId(summaryId)).build();
	}
	
	@GetMapping("/medical-record/{recordId}")
	ApiResponse<List<PrescriptionDetailResponse>> getPrescriptionDetailByMedicalRecord(@PathVariable String recordId) {
		return ApiResponse.<List<PrescriptionDetailResponse>>builder()
				.result(prescriptionDetailService.getDetailByMedicalRecord(recordId)).build();
	}
}
