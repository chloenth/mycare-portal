package com.mycareportal.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycareportal.appointment.dto.request.AppointmentCreationRequest;
import com.mycareportal.appointment.dto.response.AppointmentResponse;
import com.mycareportal.appointment.exception.AppException;
import com.mycareportal.appointment.exception.ErrorCode;
import com.mycareportal.appointment.mapper.AppointmentMapper;
import com.mycareportal.appointment.repository.AppointmentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentService {
	AppointmentRepository appointmentRepository;
	AppointmentMapper appointmentMapper;

	public AppointmentResponse createAppointment(AppointmentCreationRequest request) {
		var appointment = appointmentMapper.toAppointment(request);
		return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
	}

	public AppointmentResponse getAppointment(String appointmentId) {
		var appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

		return appointmentMapper.toAppointmentResponse(appointment);
	}

	public List<AppointmentResponse> getAppointmentsByPatient(String patientId) {
		return appointmentRepository.findByPatientId(patientId).stream().map(appointmentMapper::toAppointmentResponse)
				.toList();
	}

}
