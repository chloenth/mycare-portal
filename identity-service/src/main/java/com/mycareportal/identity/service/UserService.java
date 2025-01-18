package com.mycareportal.identity.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycareportal.identity.dto.request.UserCreationRequest;
import com.mycareportal.identity.dto.request.UserUpdateRequest;
import com.mycareportal.identity.dto.response.UserResponse;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.exception.AppException;
import com.mycareportal.identity.exception.ErrorCode;
import com.mycareportal.identity.mapper.ProfileMapper;
import com.mycareportal.identity.mapper.UserMapper;
import com.mycareportal.identity.repository.RoleRepository;
import com.mycareportal.identity.repository.UserRepository;
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

	// create new user
	public UserResponse createUser(UserCreationRequest request) {
		// check if username is existed
		boolean usernameExists = userRepository.existsByUsername(request.getUsername());
		if (usernameExists) {
			throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
		}

		// create new user if not existed
		User user = userMapper.toUser(request);

		var roles = roleRepository.findAllById(request.getRoles());
		user.setRoles(new HashSet<>(roles));
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user = userRepository.save(user);
		
		var profileRequest = profileMapper.toProfileCreationRequest(request);
		profileRequest.setUserId(user.getId());
		
		var profileResponse = profileClient.createProfile(profileRequest);
		log.info("profile request: {}", profileResponse.toString());

		return userMapper.toUserResponse(user);
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
}
