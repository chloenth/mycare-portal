package com.mycareportal.identity.dto.request;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {	
	String userId;
	String email;	
	String firstName;
	String lastName;	
	LocalDate dob;
	String gender;
	String address;
	String phoneNumber;
}
