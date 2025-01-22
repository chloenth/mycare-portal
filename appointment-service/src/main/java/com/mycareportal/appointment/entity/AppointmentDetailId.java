package com.mycareportal.appointment.entity;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentDetailId implements Serializable {
	private static final long serialVersionUID = 1L; 
	
	String appointment;
	String departmentId;
}
