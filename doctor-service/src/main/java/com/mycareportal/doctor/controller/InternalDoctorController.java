package com.mycareportal.doctor.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.doctor.dto.ApiResponse;
import com.mycareportal.doctor.dto.request.DoctorCreationRequest;
import com.mycareportal.doctor.dto.response.DoctorResponse;
import com.mycareportal.doctor.service.DoctorService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalDoctorController {
	DoctorService doctorService;

	 @PostMapping("/internal/users")
	ApiResponse<DoctorResponse> createDoctor(@RequestBody DoctorCreationRequest request) {
		return ApiResponse.<DoctorResponse>builder().result(doctorService.createDoctor(request)).build();
	}
}
