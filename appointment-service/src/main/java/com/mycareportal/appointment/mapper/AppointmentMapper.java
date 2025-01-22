package com.mycareportal.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.appointment.dto.request.AppointmentCreationRequest;
import com.mycareportal.appointment.dto.response.AppointmentResponse;
import com.mycareportal.appointment.entity.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
	@Mapping(target = "id", ignore = true)
	Appointment toAppointment(AppointmentCreationRequest request);

	AppointmentResponse toAppointmentResponse(Appointment appointment);
}
