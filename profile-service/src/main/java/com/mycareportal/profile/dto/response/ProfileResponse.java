package com.mycareportal.profile.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    Long id;
    Long userId;
    String fullName;
    String email;
    LocalDate dob;
    String gender;
    String address;
    String phoneNumber;
    String avatar;
}
