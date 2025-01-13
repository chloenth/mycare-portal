package com.mycareportal.identity.validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		return value != null && value.isBefore(LocalDate.now());
	}

}
