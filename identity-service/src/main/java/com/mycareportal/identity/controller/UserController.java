package com.mycareportal.identity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.identity.dto.ApiResponse;
import com.mycareportal.identity.dto.request.UserCreationRequest;
import com.mycareportal.identity.dto.request.UserUpdateRequest;
import com.mycareportal.identity.dto.response.UserResponse;
import com.mycareportal.identity.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
	UserService userService;

	@PostMapping("/registration")
	ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
		return ApiResponse.<UserResponse>builder().result(userService.createUser(request)).build();
	}

	@GetMapping
	ApiResponse<List<UserResponse>> getUsers() {
		return ApiResponse.<List<UserResponse>>builder().result(userService.getUsers()).build();
	}

	@PutMapping("/{userId}")
	ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
		return ApiResponse.<UserResponse>builder().result(userService.updateUser(userId, request)).build();
	}

	@DeleteMapping("/{userId}")
	ApiResponse<String> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		return ApiResponse.<String>builder().result("User has been deleted").build();
	}
}
