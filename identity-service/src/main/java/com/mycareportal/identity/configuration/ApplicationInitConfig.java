package com.mycareportal.identity.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mycareportal.identity.constant.PredefinedRole;
import com.mycareportal.identity.entity.Role;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.repository.RoleRepository;
import com.mycareportal.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Configuration
public class ApplicationInitConfig {
	PasswordEncoder passwordEncoder;

	@NonFinal
	static final String ADMIN_USERNAME = "admin";

	@NonFinal
	static final String ADMIN_PASSWORD = "admin";
	
	@Bean
	ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
		log.info("Initializing application.....");
		return args -> {
			if (userRepository.findByUsername(null).isEmpty()) {
				roleRepository
						.save(Role.builder().name(PredefinedRole.DOCTOR_ROLE).desscription("Doctor role").build());
				roleRepository
						.save(Role.builder().name(PredefinedRole.PATIENT_ROLE).desscription("Patient role").build());

				Role adminRole = roleRepository
						.save(Role.builder().name(PredefinedRole.ADMIN_ROLE).desscription("Admin role").build());

				Set<Role> roles = new HashSet<>();
				roles.add(adminRole);

				User user = User.builder().username(ADMIN_USERNAME)
						.password(passwordEncoder.encode(ADMIN_PASSWORD)).roles(roles).build();
				userRepository.save(user);
				log.warn("admin user has been created with default password: admin, please change it");
			}
			log.info("Application initialization completed .....");
		};
	}
}
