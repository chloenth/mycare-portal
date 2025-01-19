package com.mycareportal.doctor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.doctor.dto.ApiResponse;
import com.mycareportal.doctor.dto.response.DoctorResponse;
import com.mycareportal.doctor.service.DoctorService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorController {
	DoctorService doctorService;
	
	@GetMapping
	ApiResponse<List<DoctorResponse>> getAllDoctors() {
		return ApiResponse.<List<DoctorResponse>>builder().result(doctorService.getAllDoctors()).build();
	}
	
	@GetMapping("/{doctorId}")
	ApiResponse<DoctorResponse> getDoctor(@PathVariable String doctorId) {
		return ApiResponse.<DoctorResponse>builder().result(doctorService.getDoctor(doctorId)).build();
	}
	
	@DeleteMapping("/{doctorId}")
	ApiResponse<Void> deleteDoctor(@PathVariable String doctorId) {
		doctorService.deleteDoctor(doctorId);
		return ApiResponse.<Void>builder().message("Doctor has been deleted").build();
	}
}
