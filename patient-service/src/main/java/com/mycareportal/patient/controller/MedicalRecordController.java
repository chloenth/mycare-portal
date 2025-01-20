package com.mycareportal.patient.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.patient.dto.ApiResponse;
import com.mycareportal.patient.dto.response.MedicalRecordResponse;
import com.mycareportal.patient.service.MedicalRecordService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MedicalRecordController {
	MedicalRecordService medicalRecordService;
	
	@GetMapping("/{patientId}/medical-records")
	ApiResponse<List<MedicalRecordResponse>> getPatientMedicalHistory(@PathVariable String patientId) {
		return ApiResponse.<List<MedicalRecordResponse>>builder()
				.result(medicalRecordService.getPatientMedicalHistory(patientId)).build();
	}
}
