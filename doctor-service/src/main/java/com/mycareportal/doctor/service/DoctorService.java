package com.mycareportal.doctor.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mycareportal.doctor.dto.request.DoctorCreationRequest;
import com.mycareportal.doctor.dto.response.DoctorResponse;
import com.mycareportal.doctor.entity.Department;
import com.mycareportal.doctor.entity.Doctor;
import com.mycareportal.doctor.exception.AppException;
import com.mycareportal.doctor.exception.ErrorCode;
import com.mycareportal.doctor.mapper.DoctorMapper;
import com.mycareportal.doctor.repository.DepartmentRepository;
import com.mycareportal.doctor.repository.DoctorRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorService {
	DoctorRepository doctorRepository;
	DepartmentRepository departmentRepository;
	DoctorMapper doctorMapper;

	public DoctorResponse createDoctor(DoctorCreationRequest request) {
		Doctor doctor = doctorMapper.toDoctor(request);
		Department department = departmentRepository.findById(request.getDepartment()).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
		doctor.setDepartment(department);
		
		return doctorMapper.toDoctorResponse(doctorRepository.save(doctor));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public List<DoctorResponse> getAllDoctors() {
		return doctorRepository.findAll().stream().map(doctorMapper::toDoctorResponse).toList();
	}

	public DoctorResponse getDoctor(String doctorId) {
		return doctorRepository.findById(doctorId).map(doctorMapper::toDoctorResponse)
				.orElseThrow(() -> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteDoctor(String doctorId) {
		doctorRepository.deleteById(doctorId);
	}
}
