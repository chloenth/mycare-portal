package com.mycareportal.pharmacy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycareportal.pharmacy.dto.reponse.PrescriptionDetailResponse;
import com.mycareportal.pharmacy.dto.request.PrescriptionDetailCreationRequest;
import com.mycareportal.pharmacy.exception.AppException;
import com.mycareportal.pharmacy.exception.ErrorCode;
import com.mycareportal.pharmacy.mapper.PrescriptionDetailMapper;
import com.mycareportal.pharmacy.repository.MedicineRepository;
import com.mycareportal.pharmacy.repository.PrescriptionDetailRepository;
import com.mycareportal.pharmacy.repository.PrescriptionSummaryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PrescriptionDetailService {
	PrescriptionDetailRepository prescriptionDetailRepository;
	PrescriptionDetailMapper prescriptionDetailMapper;

	PrescriptionSummaryRepository prescriptionSummaryRepository;
	MedicineRepository medicineRepository;

	public PrescriptionDetailResponse createPrescriptionDetail(PrescriptionDetailCreationRequest request) {
		var prescriptionDetail = prescriptionDetailMapper.toPrescriptionDetail(request);

		var summary = prescriptionSummaryRepository.findById(request.getPrescriptionSummaryId())
				.orElseThrow(() -> new AppException(ErrorCode.PRESCRIPTION_SUMMARY_NOT_FOUND));

		var medicine = medicineRepository.findById(request.getMedicineId())
				.orElseThrow(() -> new AppException(ErrorCode.MEDICINE_NOT_FOUND));

		prescriptionDetail.setPrescriptionSummary(summary);
		prescriptionDetail.setMedicine(medicine);

		return prescriptionDetailMapper
				.toPrescriptionDetailResponse(prescriptionDetailRepository.save(prescriptionDetail));
	}

	public List<PrescriptionDetailResponse> getDetailBySummaryId(String prescriptionId) {
		return prescriptionDetailRepository.findByPrescriptionSummary_Id(prescriptionId).stream()
				.map(prescriptionDetailMapper::toPrescriptionDetailResponse).toList();
	}

	public List<PrescriptionDetailResponse> getDetailByMedicalRecord(String recordId) {
		return prescriptionDetailRepository.findByMedicalRecordId(recordId).stream()
				.map(prescriptionDetailMapper::toPrescriptionDetailResponse).toList();
	}
}
