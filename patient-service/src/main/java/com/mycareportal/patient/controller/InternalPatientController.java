package com.mycareportal.patient.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.patient.dto.ApiResponse;
import com.mycareportal.patient.dto.request.PatientCreationRequest;
import com.mycareportal.patient.dto.response.PatientResponse;
import com.mycareportal.patient.service.PatientService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalPatientController {
	PatientService patientService;

	 @PostMapping("/internal/users")
	ApiResponse<PatientResponse> createDoctor(@RequestBody PatientCreationRequest request) {
		return ApiResponse.<PatientResponse>builder().result(patientService.createPatient(request)).build();
	}
}
