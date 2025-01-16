package com.mycareportal.profile.entity;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Node("user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    String userId;

    String firstName;
    String lastName;
    String email;
    LocalDate dob;
    String gender;
    String address;
    String phoneNumber;
}
