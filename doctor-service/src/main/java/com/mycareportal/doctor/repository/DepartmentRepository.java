package com.mycareportal.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.doctor.entity.Department;
import com.mycareportal.doctor.entity.Doctor;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

}
