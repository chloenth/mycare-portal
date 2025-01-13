package com.mycareportal.identity.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.mycareportal.identity.constant.PredefinedRole;
import com.mycareportal.identity.dto.request.UserCreationRequest;
import com.mycareportal.identity.dto.response.UserResponse;
import com.mycareportal.identity.entity.Role;
import com.mycareportal.identity.entity.User;
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
	
	public UserResponse createUser(UserCreationRequest request) {
		User user = userMapper.toUser(request);
		
		Set<Role> roles = new HashSet<>();
		roleRepository.findById(PredefinedRole.PATIENT_ROLE).ifPresent(roles::add);
		user.setRoles(roles);
		
		user = userRepository.save(user);
		
		return userMapper.toUserResponse(user);
	}
}
