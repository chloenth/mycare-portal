package com.mycareportal.identity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycareportal.identity.dto.request.role.RoleRequest;
import com.mycareportal.identity.dto.response.role.RoleResponse;
import com.mycareportal.identity.entity.Role;
import com.mycareportal.identity.mapper.RoleMapper;
import com.mycareportal.identity.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
	RoleRepository roleRepository;
	RoleMapper roleMapper;

	public RoleResponse createRole(RoleRequest request) {
		Role role = roleMapper.toRole(request);
		role = roleRepository.save(role);

		return roleMapper.toRoleResponse(role);
	}

	public List<RoleResponse> getRoles() {
		return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
	}
	
	public void deleteRole(String role) {
		roleRepository.deleteById(role);
	}
}
