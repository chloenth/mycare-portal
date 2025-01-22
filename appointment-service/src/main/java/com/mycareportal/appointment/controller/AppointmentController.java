package com.mycareportal.appointment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.appointment.dto.ApiResponse;
import com.mycareportal.appointment.dto.request.AppointmentCreationRequest;
import com.mycareportal.appointment.dto.response.AppointmentResponse;
import com.mycareportal.appointment.service.AppointmentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentController {
	AppointmentService appointmentService;

	@PostMapping
	ApiResponse<AppointmentResponse> createAppointment(@RequestBody AppointmentCreationRequest request) {
		return ApiResponse.<AppointmentResponse>builder().result(appointmentService.createAppointment(request)).build();
	}

	@GetMapping("/{appointmentId}")
	ApiResponse<AppointmentResponse> getAppointment(@PathVariable String appointmentId) {
		return ApiResponse.<AppointmentResponse>builder().result(appointmentService.getAppointment(appointmentId))
				.build();
	}

	@GetMapping("/patients/{patientId}")
	ApiResponse<List<AppointmentResponse>> getAppointmentsByPatient(@PathVariable String patientId) {
		return ApiResponse.<List<AppointmentResponse>>builder()
				.result(appointmentService.getAppointmentsByPatient(patientId)).build();
	}
}
