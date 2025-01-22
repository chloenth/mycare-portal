package com.mycareportal.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycareportal.appointment.dto.request.AppointmentDetailCreationRequest;
import com.mycareportal.appointment.dto.response.AppointmentDetailResponse;
import com.mycareportal.appointment.exception.AppException;
import com.mycareportal.appointment.exception.ErrorCode;
import com.mycareportal.appointment.mapper.AppointmentDetailMapper;
import com.mycareportal.appointment.repository.AppointmentDetailRepository;
import com.mycareportal.appointment.repository.AppointmentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentDetailService {
	AppointmentDetailRepository appointmentDetailRepository;
	AppointmentDetailMapper appointmentDetailMapper;

	AppointmentRepository appointmentRepository;

	public AppointmentDetailResponse createAppointmentDetail(String appointmentId,
			AppointmentDetailCreationRequest request) {
		var appointmentDetail = appointmentDetailMapper.toAppointmentDetail(request);

		var appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

		appointmentDetail.setAppointment(appointment);

		return appointmentDetailMapper.toAppointmentDetailResponse(appointmentDetailRepository.save(appointmentDetail));
	}

	public List<AppointmentDetailResponse> getAppointmentDetailByAppointment(String appointmentId) {
		return appointmentDetailRepository.findByAppointment_Id(appointmentId).stream()
				.map(appointmentDetailMapper::toAppointmentDetailResponse).toList();
	}

	public List<AppointmentDetailResponse> getAppointmentDetailByDepartment(String deparmentId) {
		return appointmentDetailRepository.findByDepartmentId(deparmentId).stream()
				.map(appointmentDetailMapper::toAppointmentDetailResponse).toList();
	}
}
