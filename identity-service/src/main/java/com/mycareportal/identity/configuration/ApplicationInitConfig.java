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
			// create doctor, patient, admin role if not exists
			createRoleIfNotExists(roleRepository, PredefinedRole.DOCTOR_ROLE, "Doctor role");
			createRoleIfNotExists(roleRepository, PredefinedRole.PATIENT_ROLE, "Patient role");
			createRoleIfNotExists(roleRepository, PredefinedRole.ADMIN_ROLE, "Admin role");

			// create admin account if not exists
			if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {

				Set<Role> roles = new HashSet<>();
				roleRepository.findById(PredefinedRole.ADMIN_ROLE).ifPresent(roles::add);

				User user = User.builder().username(ADMIN_USERNAME).password(passwordEncoder.encode(ADMIN_PASSWORD))
						.roles(roles).build();
				userRepository.save(user);
				log.warn("admin user has been created with default password: admin, please change it");
			}
			log.info("Application initialization completed .....");
		};

	}

	private void createRoleIfNotExists(RoleRepository roleRepository, String roleName, String description) {
		if (roleRepository.findById(roleName).isEmpty()) {
			roleRepository.save(Role.builder().name(roleName).desscription(description).build());
		}
	}
}
