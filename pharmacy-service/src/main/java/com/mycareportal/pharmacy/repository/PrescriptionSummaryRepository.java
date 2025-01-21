package com.mycareportal.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.pharmacy.entity.PrescriptionSummary;

@Repository
public interface PrescriptionSummaryRepository extends JpaRepository<PrescriptionSummary, String> {
	
}
