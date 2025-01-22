package com.mycareportal.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.appointment.entity.AppointmentDetail;
import com.mycareportal.appointment.entity.AppointmentDetailId;

@Repository
public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetail, AppointmentDetailId> {
	List<AppointmentDetail> findByAppointment_Id(String appointmentId);
	List<AppointmentDetail> findByDepartmentId(String departmentId);
}