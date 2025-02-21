package com.mycareportal.appointment.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentCreationRequest {
	String patientId;
	String doctorId;
	String departmentId;
	LocalDate appointmentDate;
	LocalTime time;
	LocalDate createdDate;
	String status;
}
