package com.mycareportal.identity.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycareportal.identity.dto.request.UserCreationRequest;
import com.mycareportal.identity.dto.request.UserUpdateRequest;
import com.mycareportal.identity.dto.response.UserResponse;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.exception.AppException;
import com.mycareportal.identity.exception.ErrorCode;
import com.mycareportal.identity.mapper.UserMapper;
import com.mycareportal.identity.repository.RoleRepository;
import com.mycareportal.identity.repository.UserRepository;

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

	public UserResponse createUser(UserCreationRequest request) {
		// check if username is existed
		boolean usernameExists = userRepository.existsByUsername(request.getUsername());
		if (usernameExists) {
		    throw new AppException(ErrorCode.USER_EXISTED);
		}
		
		// create new user if not existed
		User user = userMapper.toUser(request);

		var roles = roleRepository.findAllById(request.getRoles());
		user.setRoles(new HashSet<>(roles));

		user = userRepository.save(user);

		return userMapper.toUserResponse(user);
	}

	public List<UserResponse> getUsers() {
		return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
	}

	public UserResponse updateUser(String id, UserUpdateRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("No user found"));
		userMapper.updateUser(user, request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		var roles = roleRepository.findAllById(request.getRoles());
		user.setRoles(new HashSet<>(roles));

		return userMapper.toUserResponse(userRepository.save(user));
	}
	
	public void deleteUser(String userId) {
		if (!userRepository.existsById(userId)) {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
		userRepository.deleteById(userId);
	}
}
