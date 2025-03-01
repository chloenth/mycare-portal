package com.mycareportal.identity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.user.UserResponse;
import com.mycareportal.identity.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalUserController {
	UserService userService;

	// Get All Users
	@GetMapping
	ApiResponse<List<UserResponse>> getAllUsers() {
		return ApiResponse.<List<UserResponse>>builder().result(userService.getAllUsers()).build();
	}
}
