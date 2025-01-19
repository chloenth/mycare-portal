package com.mycareportal.patient.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mycareportal.patient.dto.request.PatientCreationRequest;
import com.mycareportal.patient.dto.response.PatientResponse;
import com.mycareportal.patient.exception.AppException;
import com.mycareportal.patient.exception.ErrorCode;
import com.mycareportal.patient.mapper.PatientMapper;
import com.mycareportal.patient.repository.PatientRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientService {
	PatientRepository patientRepository;
	PatientMapper patientMapper;
	
	public PatientResponse createPatient(PatientCreationRequest request) {
		var patient = patientMapper.toPatient(request);
		patient.setCreatedDate(LocalDate.now());		
		return patientMapper.toPatientResponse(patientRepository.save(patient));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public List<PatientResponse> getAllDoctors() {
		return patientRepository.findAll().stream().map(patientMapper::toPatientResponse).toList();
	}

	public PatientResponse getDoctor(String doctorId) {
		return patientRepository.findById(doctorId).map(patientMapper::toPatientResponse)
				.orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteDoctor(String doctorId) {
		patientRepository.deleteById(doctorId);
	}
}
