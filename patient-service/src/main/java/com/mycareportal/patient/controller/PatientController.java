package com.mycareportal.patient.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.patient.dto.ApiResponse;
import com.mycareportal.patient.dto.response.PatientResponse;
import com.mycareportal.patient.service.PatientService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientController {
	PatientService patientService;
	
	@GetMapping
	ApiResponse<List<PatientResponse>> getAllPatients() {
		return ApiResponse.<List<PatientResponse>>builder().result(patientService.getAllDoctors()).build();
	}
	
	@GetMapping("/{patientId}")
	ApiResponse<PatientResponse> getPatient(@PathVariable String patientId) {
		return ApiResponse.<PatientResponse>builder().result(patientService.getDoctor(patientId)).build();
	}
	
	@DeleteMapping("/{patientId}")
	ApiResponse<Void> deletePatient(@PathVariable String patientId) {
		patientService.deleteDoctor(patientId);
		return ApiResponse.<Void>builder().message("Patient has been deleted").build();
	}
}
