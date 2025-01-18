package com.mycareportal.doctor.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycareportal.doctor.constant.PredefinedDepartment;
import com.mycareportal.doctor.entity.Department;
import com.mycareportal.doctor.repository.DepartmentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Configuration
public class ApplicationInitConfig {

	@Bean
	ApplicationRunner applicationRunner(DepartmentRepository departmentRepository) {
		log.info("Initializing application.....");
		return args -> {
			// create department if not exists
			createDepartmentIfNotExists(departmentRepository, PredefinedDepartment.INTERNAL_MEDICINE, "Internal Medicine");
			createDepartmentIfNotExists(departmentRepository, PredefinedDepartment.SURGERY_DEPARTMENT, "Surgery Department");
			createDepartmentIfNotExists(departmentRepository, PredefinedDepartment.PEDIATRICS_DEPARTMENT, "Pediatrics Department");
			createDepartmentIfNotExists(departmentRepository, PredefinedDepartment.CARDIOLOGY_DEPARTMENT, "Cardiology Department");
			createDepartmentIfNotExists(departmentRepository, PredefinedDepartment.ENT_DEPARTMENT, "ENT (Ear, Nose, and Throat) Department");
			createDepartmentIfNotExists(departmentRepository, PredefinedDepartment.OPHTHALMOLOGY_DEPARTMENT, "Ophthalmology Department");
		
			log.info("Application initialization completed .....");
		};

	}

	private void createDepartmentIfNotExists(DepartmentRepository departmentRepository, String departmentName, String description) {
		if (departmentRepository.findById(departmentName).isEmpty()) {
			departmentRepository.save(Department.builder().name(departmentName).description(description).build());
		}
	}
}
