package com.mycareportal.identity.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycareportal.event.dto.KafkaMessage;
import com.mycareportal.identity.constant.PredefinedRole;
import com.mycareportal.identity.dto.request.kafka.UsernameKafkaUpdateRequest;
import com.mycareportal.identity.dto.request.user.PasswordUpdateRequest;
import com.mycareportal.identity.dto.request.user.UserCreationRequest;
import com.mycareportal.identity.dto.request.user.UsernameUpdateRequest;
import com.mycareportal.identity.dto.request.userprofile.UserProfileRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;
import com.mycareportal.identity.dto.response.pagedata.user.UserWithProfileResponse;
import com.mycareportal.identity.dto.response.user.UserResponse;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.exception.AppException;
import com.mycareportal.identity.exception.ErrorCode;
import com.mycareportal.identity.mapper.DoctorMapper;
import com.mycareportal.identity.mapper.PatientMapper;
import com.mycareportal.identity.mapper.ProfileMapper;
import com.mycareportal.identity.mapper.UserMapper;
import com.mycareportal.identity.repository.RoleRepository;
import com.mycareportal.identity.repository.UserRepository;
import com.mycareportal.identity.repository.httpclient.DoctorClient;
import com.mycareportal.identity.repository.httpclient.PatientClient;
import com.mycareportal.identity.repository.httpclient.ProfileClient;
import com.mycareportal.identity.service.kafka.KafkaProducerService;

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

	PasswordEncoder passwordEncoder;

	ProfileClient profileClient;
	ProfileMapper profileMapper;
	UserMapper userMapper;
	ObjectMapper objectMapper;

	KafkaProducerService kafkaProducerService;

	int PAGE_SIZE = 5;
	String USERNAME = "username";
	String ASCENDING = "asc";

	DoctorMapper doctorMapper;
	DoctorClient doctorClient;

	PatientMapper patientMapper;
	PatientClient patientClient;

	// create new user
	public UserWithProfileResponse createUser(UserCreationRequest userRequest, MultipartFile avatarFile) {
		// check if username is existed
		boolean usernameExists = userRepository.existsByUsername(userRequest.getUsername());
		if (usernameExists) {
			throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
		}

		User user = userMapper.toUser(userRequest);

		var roleInDB = roleRepository.findById(PredefinedRole.USER_ROLE)
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
		user.setRoles(new HashSet<>(List.of(roleInDB)));
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user = userRepository.save(user);

//		Create Profile
		var profileRequest = profileMapper.toProfileCreationRequest(userRequest);
		profileRequest.setUserId(user.getId());
		log.info("phoneNumber in request in user service: {}", profileRequest.getPhoneNumber());

		ApiResponse<ProfileResponse> profileResponse = null;

		try {
			String profileJson = objectMapper.writeValueAsString(profileRequest);
			profileResponse = profileClient.createProfile(avatarFile, profileJson);

			log.info("avatar in repsonse in user service: {}", profileResponse.getResult().getAvatar());

			UserWithProfileResponse userWithProfileResponse = userMapper.toUserWithProfileResponse(user,
					profileResponse.getResult());

			kafkaProducerService.sendUserCreatedEvent(KafkaMessage.<UserProfileRequest>builder().type("UserCreation")
					.payload(userMapper.toUserProfileRequest(userWithProfileResponse)).build());

			return userWithProfileResponse;

		} catch (Exception e) {
			// roll back if profile creation fail
			if (profileResponse == null || profileResponse.getCode() != 1000) {
				userRepository.delete(user);
			}

			throw new RuntimeException("Failed to create user", e);
		}

	}

	// create new user with patient role
//	public UserResponse createUserWithPatientRole(UserCreationRequest request) {
//		// create new user if not existed
//		User user = createUser(request, PredefinedRole.PATIENT_ROLE);
//		
//		ApiResponse<UserProfileResponse> profileResponse = null;
//		ApiResponse<PatientResponse> patientResponse = null;
//
//		try {
//			profileResponse = createProfile(request, user.getId());
//			
//			var patientRequest = patientMapper.toPatientCreationRequest(request);
//			patientRequest.setProfileId(profileResponse.getResult().getId());
//			patientRequest.setUserId(user.getId());
//			
//			patientResponse = patientClient.createPatient(patientRequest);
//
//			log.info("profileResponse: {}", profileResponse.toString());
//			log.info("doctorResponse: {}", patientResponse.toString());
//		} catch (Exception e) {
//			// roll back if profile creation fail or doctor creation fail
//			userRepository.delete(user);
//
//			if (profileResponse != null && profileResponse.getResult() != null) {
//				profileClient.deleteProfile(profileResponse.getResult().getId());
//			}
//
//			throw new RuntimeException("Fail to create user");
//		}
//
//		var userResponse = userMapper.toUserResponse(user);
//		userResponse.setProfileId(profileResponse.getResult().getId());
//		userResponse.setPatientId(patientResponse.getResult().getId());
//
//		return userResponse;	
//	}

	// create new user with doctor role
//	public UserResponse createUserWithDoctorRole(UserCreationRequest request) {
//		// create new user if not existed
//		User user = createUser(request, PredefinedRole.DOCTOR_ROLE);
//
//		ApiResponse<UserProfileResponse> profileResponse = null;
//		ApiResponse<DoctorResponse> doctorResponse = null;
//
//		try {
//			profileResponse = createProfile(request, user.getId());
//			
//			var doctorRequest = doctorMapper.toDoctorCreationRequest(request);
//			doctorRequest.setProfileId(profileResponse.getResult().getId());
//			doctorRequest.setUserId(user.getId());
//			
//			doctorResponse = doctorClient.createDoctor(doctorRequest);
//
//			log.info("profileResponse: {}", profileResponse.toString());
//			log.info("doctorResponse: {}", doctorResponse.toString());
//		} catch (Exception e) {
//			// roll back if profile creation fail or doctor creation fail
//			userRepository.delete(user);
//
//			if (profileResponse != null && profileResponse.getResult() != null) {
//				profileClient.deleteProfile(profileResponse.getResult().getId());
//			}
//
//			throw new RuntimeException("Fail to create user");
//		}
//
//		var userResponse = userMapper.toUserResponse(user);
//		userResponse.setProfileId(profileResponse.getResult().getId());
//		userResponse.setDoctorId(doctorResponse.getResult().getId());
//
//		return userResponse;
//	}

	// get all users
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
	}

	public UserResponse getMyInfo() {
		log.info("access to user service myinfo");
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var username = authentication.getName();

		log.info("username: ", username);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		return userMapper.toUserResponse(user);
	}

	// update username
	public UserResponse updateUsername(Long id, UsernameUpdateRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		String newUsername = request.getUsername();

		boolean usernameExists = userRepository.existsByUsername(newUsername);
		if (usernameExists) {
			throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
		}

		user.setUsername(newUsername);

		UserResponse userResponse = userMapper.toUserResponse(userRepository.save(user));

		UsernameKafkaUpdateRequest kafkaRequest = UsernameKafkaUpdateRequest.builder()
				.username(userResponse.getUsername()).userId(userResponse.getId()).build();

		kafkaProducerService.sendUsernameUpdatedEvent(KafkaMessage.<UsernameKafkaUpdateRequest>builder()
				.type("UsernameUpdate").payload(kafkaRequest).build());

		return userResponse;
	}

	// update password
	public UserResponse updatePassword(Long id, PasswordUpdateRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		String newPassword = request.getPassword();

		user.setPassword(passwordEncoder.encode(newPassword));

		return userMapper.toUserResponse(userRepository.save(user));
	}

	// delete a user
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
		userRepository.deleteById(userId);
	}

}
