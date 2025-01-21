package com.mycareportal.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.pharmacy.entity.PrescriptionDetail;
import com.mycareportal.pharmacy.entity.idclass.PrescriptionDetailId;

@Repository
public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, PrescriptionDetailId> {
	List<PrescriptionDetail> findByPrescriptionSummary_Id(String summaryId);
	List<PrescriptionDetail> findByMedicalRecordId(String recordId);
}
