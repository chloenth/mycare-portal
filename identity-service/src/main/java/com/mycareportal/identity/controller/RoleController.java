package com.mycareportal.identity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.identity.dto.request.role.RoleRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.role.RoleResponse;
import com.mycareportal.identity.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
	RoleService roleService;

	@PostMapping
	ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
		return ApiResponse.<RoleResponse>builder().result(roleService.createRole(request)).build();
	}
	
	@GetMapping
	ApiResponse<List<RoleResponse>> getRoles() {
		return ApiResponse.<List<RoleResponse>>builder().result(roleService.getRoles()).build();
	}
	
	@DeleteMapping("/{role}")
	ApiResponse<Void> delete(@PathVariable String role) {
		roleService.deleteRole(role.toUpperCase());
		return ApiResponse.<Void>builder().build();
	}
}
