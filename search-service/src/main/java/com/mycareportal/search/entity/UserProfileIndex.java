package com.mycareportal.search.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.mycareportal.search.dto.response.role.RoleResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "user-profile")
public class UserProfileIndex {
	@Id
	Long id;

	String username;

	@Field(type = FieldType.Nested)
	Set<RoleResponse> roles;

	Long profileId;
	String fullName;

	Long dob;

	String gender;
	String avatar;
	String email;
	String phoneNumber;
	String address;

	public void setDobToLong(LocalDate localDate) {
		if (localDate == null) {
			return;
		}
		this.dob = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public String getDobAsString() {
		if(this.dob == null) {
			return null;
		}
		
		return Instant.ofEpochMilli(this.dob).atZone(ZoneId.systemDefault()).toLocalDate().toString();
	}

//	public String getDobAsString() {
//		if (dob == null) {
//			return null; // Return null if dob is not set
//		}
//
//		// Convert the long timestamp to a LocalDate
//		LocalDate localDate = getDobAsLocalDate();
//
//		// Use DateTimeFormatter to format the date
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		return localDate.format(formatter); // Format and return the date as string
//	}

}
