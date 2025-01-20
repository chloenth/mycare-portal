package com.mycareportal.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycareportal.patient.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
	@Query("SELECT mr FROM MedicalRecord mr JOIN mr.visitSession vs WHERE vs.patient.id = :patientId")
	List<MedicalRecord> findAllMedicalRecordsForPatient(@Param("patientId") String patientId);
}
