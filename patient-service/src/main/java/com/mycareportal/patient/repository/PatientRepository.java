package com.mycareportal.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.patient.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

}
