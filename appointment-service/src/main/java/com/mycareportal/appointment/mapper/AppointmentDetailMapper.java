package com.mycareportal.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.appointment.dto.request.AppointmentDetailCreationRequest;
import com.mycareportal.appointment.dto.response.AppointmentDetailResponse;
import com.mycareportal.appointment.entity.AppointmentDetail;

@Mapper(componentModel = "spring")
public interface AppointmentDetailMapper {
	@Mapping(target = "appointment", ignore = true)
	AppointmentDetail toAppointmentDetail(AppointmentDetailCreationRequest request);

	@Mapping(target = "appointmentId", source = "appointment.id")
	AppointmentDetailResponse toAppointmentDetailResponse(AppointmentDetail appointmentDetail);
}
