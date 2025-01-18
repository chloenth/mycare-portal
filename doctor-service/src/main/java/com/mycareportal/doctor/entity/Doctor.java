package com.mycareportal.doctor.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;
	String userId;
	String profileId;
	String specialization;
	
	@ManyToOne
	@JoinColumn(name="department_id")
	Department department;
	String education;
	LocalDate dateOfJoining;
	String title;
	Long salary;
}
