package com.mycareportal.identity.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycareportal.event.dto.KafkaMessage;
import com.mycareportal.identity.constant.PredefinedRole;
import com.mycareportal.identity.dto.ApiResponse;
import com.mycareportal.identity.dto.request.ProfileCreationRequest;
import com.mycareportal.identity.dto.request.UserCreationRequest;
import com.mycareportal.identity.dto.request.UserUpdateRequest;
import com.mycareportal.identity.dto.response.DoctorResponse;
import com.mycareportal.identity.dto.response.UserProfileResponse;
import com.mycareportal.identity.dto.response.UserResponse;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.exception.AppException;
import com.mycareportal.identity.exception.ErrorCode;
import com.mycareportal.identity.mapper.DoctorMapper;
import com.mycareportal.identity.mapper.ProfileMapper;
import com.mycareportal.identity.mapper.UserMapper;
import com.mycareportal.identity.repository.RoleRepository;
import com.mycareportal.identity.repository.UserRepository;
import com.mycareportal.identity.repository.httpclient.DoctorClient;
import com.mycareportal.identity.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
	UserRepository userRepository;
	RoleRepository roleRepository;
	UserMapper userMapper;
	PasswordEncoder passwordEncoder;
	ProfileClient profileClient;
	ProfileMapper profileMapper;
	DoctorMapper doctorMapper;
	DoctorClient doctorClient;

	// create new user with patient role
	public UserResponse createUserWithPatientRole(UserCreationRequest request) {
		// check if username is existed
		checkUsernameExisted(request.getUsername());

		// create new user if not existed
		User user = userMapper.toUser(request);

		var role = roleRepository.findById(PredefinedRole.PATIENT_ROLE)
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
		user.setRoles(new HashSet<>(List.of(role)));
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user = userRepository.save(user);

		var profileRequest = profileMapper.toProfileCreationRequest(request);
		profileRequest.setUserId(user.getId());
		var profileResponse = profileClient.createProfile(profileRequest);
		
		var userResponse = userMapper.toUserResponse(user);
		userResponse.setProfileId(profileResponse.getResult().getId());
		

		return userResponse;
	}

	// create new user with doctor role
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponse createUserWithDoctorRole(UserCreationRequest request) {
		// check if username is existed
		checkUsernameExisted(request.getUsername());

		// create new user if not existed
		User user = userMapper.toUser(request);

		var role = roleRepository.findById(PredefinedRole.DOCTOR_ROLE)
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
		user.setRoles(new HashSet<>(List.of(role)));
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user = userRepository.save(user);

		var profileRequest = profileMapper.toProfileCreationRequest(request);
		profileRequest.setUserId(user.getId());

		ApiResponse<UserProfileResponse> profileResponse = null;
		ApiResponse<DoctorResponse> doctorResponse = null;

		try {
			profileResponse = profileClient.createProfile(profileRequest);
			
			var doctorRequest = doctorMapper.toDoctorCreationRequest(request);
			doctorRequest.setProfileId(profileResponse.getResult().getId());
			doctorRequest.setUserId(user.getId());
			
			doctorResponse = doctorClient.createDoctor(doctorRequest);

			log.info("profileResponse: {}", profileResponse.toString());
			log.info("doctorResponse: {}", doctorResponse.toString());
		} catch (Exception e) {
			// roll back if profile creation fail or doctor creation fail
			userRepository.delete(user);

			if (profileResponse != null && profileResponse.getResult() != null) {
				profileClient.deleteProfile(profileResponse.getResult().getId());
			}

			throw new RuntimeException("Fail to create user");
		}

		var userResponse = userMapper.toUserResponse(user);
		userResponse.setProfileId(profileResponse.getResult().getId());
		userResponse.setDoctorId(doctorResponse.getResult().getId());

		return userResponse;
	}

	// get all users
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserResponse> getUsers() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("authentication: {}", authentication);
		log.info("name: {}", authentication.getName());
		log.info("authorities: {}", authentication.getAuthorities());

		return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
	}

	public UserResponse getMyInfo() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var username = authentication.getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		return userMapper.toUserResponse(user);
	}

	// update a user
	public UserResponse updateUser(String id, UserUpdateRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		userMapper.updateUser(user, request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		var roles = roleRepository.findAllById(request.getRoles());
		user.setRoles(new HashSet<>(roles));

		return userMapper.toUserResponse(userRepository.save(user));
	}

	// delete a user
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(String userId) {
		if (!userRepository.existsById(userId)) {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
		userRepository.deleteById(userId);
	}

	private void checkUsernameExisted(String username) {
		boolean usernameExists = userRepository.existsByUsername(username);
		if (usernameExists) {
			throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
		}
	}
}
