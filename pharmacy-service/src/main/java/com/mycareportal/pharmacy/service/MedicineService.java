package com.mycareportal.pharmacy.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mycareportal.pharmacy.dto.reponse.MedicineResponse;
import com.mycareportal.pharmacy.dto.request.MedicineCreationRequest;
import com.mycareportal.pharmacy.exception.AppException;
import com.mycareportal.pharmacy.exception.ErrorCode;
import com.mycareportal.pharmacy.mapper.MedicineMapper;
import com.mycareportal.pharmacy.repository.MedicineRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MedicineService {
	MedicineRepository medicineRepository;
	MedicineMapper medicineMapper;

	public MedicineResponse addMedicine(MedicineCreationRequest request) {
		var medicine = medicineMapper.toMedicine(request);

		String batchNumber = String.format("%s-%s", request.getName().substring(0, 4).toUpperCase(),
				LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		medicine.setBatchNumber(batchNumber);

		return medicineMapper.toPharmacyResponse(medicineRepository.save(medicine));
	}

	public List<MedicineResponse> getAllMedicines() {
		return medicineRepository.findAll().stream().map(medicineMapper::toPharmacyResponse).toList();
	}

	public MedicineResponse getMedicineById(String medicineId) {
		return medicineRepository.findById(medicineId).map(medicineMapper::toPharmacyResponse)
				.orElseThrow(() -> new AppException(ErrorCode.PHARMACY_NOT_FOUND));
	}

	public List<MedicineResponse> getMedicinesByName(String medicineName) {
		return medicineRepository.findByName(medicineName).stream().map(medicineMapper::toPharmacyResponse).toList();
	}

	public List<MedicineResponse> getMedicinesByBatchNumber(String batchNumber) {
		return medicineRepository.findByName(batchNumber).stream().map(medicineMapper::toPharmacyResponse).toList();
	}
	
	public void deleteMedicineById(String medicineId) {
		medicineRepository.deleteById(medicineId);
	}
	
	public void deleteMedicineByBatchNumber(String batchNumber) {
		medicineRepository.deleteByBatchNumber(batchNumber);
	}
}
