package com.mycareportal.identity.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycareportal.identity.constant.PredefinedRole;
import com.mycareportal.identity.dto.request.profile.ProfileCreationRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;
import com.mycareportal.identity.entity.Role;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.repository.RoleRepository;
import com.mycareportal.identity.repository.UserRepository;
import com.mycareportal.identity.repository.httpclient.ProfileClient;

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
	ProfileClient profileClient;
	ObjectMapper objectMapper;

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
			createRoleIfNotExists(roleRepository, PredefinedRole.USER_ROLE, "User role");

			// create admin account if not exists
			if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {

				Set<Role> roles = new HashSet<>();
				roleRepository.findById(PredefinedRole.ADMIN_ROLE).ifPresent(roles::add);

				User user = User.builder().username(ADMIN_USERNAME).password(passwordEncoder.encode(ADMIN_PASSWORD))
						.roles(roles).build();
				user = userRepository.save(user);

				// create profile for account admin
				ProfileCreationRequest profileCreationRequest = new ProfileCreationRequest();
				profileCreationRequest.setUserId(user.getId());

				String profileRequestJson = objectMapper.writeValueAsString(profileCreationRequest);

				try {
					ApiResponse<ProfileResponse> profileResponse = profileClient.createProfile(null,
							profileRequestJson);
					
					log.info("profileResponse: {}", profileResponse);
				} catch (Exception e) {
					userRepository.delete(user);

					log.warn("failed to create admin account, try again...");
					throw e;
				}

				log.warn("admin user has been created with default password: admin, please change it");
			}
			log.info("Application initialization completed .....");
		};

	}

	private void createRoleIfNotExists(RoleRepository roleRepository, String roleName, String description) {
		if (roleRepository.findById(roleName).isEmpty()) {
			roleRepository.save(Role.builder().name(roleName).description(description).build());
		}
	}
}
