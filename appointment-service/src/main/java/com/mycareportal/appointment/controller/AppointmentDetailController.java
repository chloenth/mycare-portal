package com.mycareportal.appointment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.appointment.dto.ApiResponse;
import com.mycareportal.appointment.dto.request.AppointmentDetailCreationRequest;
import com.mycareportal.appointment.dto.response.AppointmentDetailResponse;
import com.mycareportal.appointment.service.AppointmentDetailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentDetailController {
	AppointmentDetailService appointmentDetailService;

	@PostMapping("/{appointmentId}/appointment-details")
	ApiResponse<AppointmentDetailResponse> createAppointmentDetail(@PathVariable String appointmentId,
			AppointmentDetailCreationRequest request) {
		return ApiResponse.<AppointmentDetailResponse>builder()
				.result(appointmentDetailService.createAppointmentDetail(appointmentId, request)).build();
	}

	@GetMapping("/{appointmentId}/appointment-details")
	ApiResponse<List<AppointmentDetailResponse>> getAppointmentDetailByAppointment(@PathVariable String appointmentId) {
		return ApiResponse.<List<AppointmentDetailResponse>>builder()
				.result(appointmentDetailService.getAppointmentDetailByAppointment(appointmentId)).build();
	}

	@GetMapping("/departments/{departmentId}")
	ApiResponse<List<AppointmentDetailResponse>> getAppointmentDetailByDepartment(@PathVariable String departmentId) {
		return ApiResponse.<List<AppointmentDetailResponse>>builder()
				.result(appointmentDetailService.getAppointmentDetailByDepartment(departmentId)).build();
	}
}
