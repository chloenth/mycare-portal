package com.mycareportal.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.appointment.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
	List<Appointment> findByPatientId(String patientId);
}
