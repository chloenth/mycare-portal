package com.mycareportal.pharmacy.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "batchNumber" }) })
public class Medicine {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;

	@Column(nullable = false)
	String name;
	String description;

	@Column(nullable = false)
	Long quantityInStock;

	@Column(nullable = false)
	String unit;
	LocalDate importedDate;

	@Column(nullable = false)
	LocalDate expiredDate;

	@Column(nullable = false)
	String batchNumber;
}
