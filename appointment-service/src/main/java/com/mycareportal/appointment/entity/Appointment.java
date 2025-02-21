package com.mycareportal.appointment.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	String id;
	
	String patientId;
	String doctorId;
	String departmentId;
	LocalDate appointmentDate;
	LocalTime time;
	LocalDate createdDate;
	String status;
}
