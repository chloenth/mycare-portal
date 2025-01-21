package com.mycareportal.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.pharmacy.entity.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, String> {
	List<Medicine> findByName(String name);
	List<Medicine> findByBatchNumber(String batchNumber);
	Void deleteByBatchNumber(String batchNumber);
}
