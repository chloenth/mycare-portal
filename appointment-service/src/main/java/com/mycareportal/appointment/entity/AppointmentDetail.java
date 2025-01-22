package com.mycareportal.appointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(AppointmentDetailId.class)
public class AppointmentDetail {
	@Id
	@ManyToOne
	@JoinColumn(name="appointment_id")
	Appointment appointment;
	
	@Id
	String departmentId;
	
	String doctorId;
	
	String reason;
}
