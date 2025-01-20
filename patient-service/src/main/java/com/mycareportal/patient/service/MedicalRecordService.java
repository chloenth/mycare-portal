package com.mycareportal.patient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycareportal.patient.dto.response.MedicalRecordResponse;
import com.mycareportal.patient.mapper.MedicalRecordMapper;
import com.mycareportal.patient.repository.MedicalRecordRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MedicalRecordService {
	MedicalRecordRepository medicalRecordRepository;
	MedicalRecordMapper medicalRecordMapper;

	public List<MedicalRecordResponse> getPatientMedicalHistory(String patientId) {
		return medicalRecordRepository.findAllMedicalRecordsForPatient(patientId).stream()
				.map(medicalRecordMapper::toMedicalRecordResponse).toList();
	}
}
