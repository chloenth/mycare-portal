package com.mycareportal.identity.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycareportal.identity.dto.request.user.UserCreationRequest;
import com.mycareportal.identity.dto.request.user.UserUpdateRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.pagedata.user.PageDataUserResponse;
import com.mycareportal.identity.dto.response.pagedata.user.UserWithProfileResponse;
import com.mycareportal.identity.dto.response.user.UserResponse;
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

	@PostMapping(value = "/registration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
	ApiResponse<UserWithProfileResponse> createUser(@Valid @RequestPart("user") UserCreationRequest userRequest,
			@RequestPart("avatar") MultipartFile avatarFile) {

		log.info("File uploaded: Name = {}, Type = {}, Size = {} bytes", avatarFile.getOriginalFilename(),
				avatarFile.getContentType(), avatarFile.getSize());

		return ApiResponse.<UserWithProfileResponse>builder().result(userService.createUser(userRequest, avatarFile))
				.build();
	}

//	@PostMapping("/registration-doctor")
//	ApiResponse<UserResponse> createUserWithDoctorRole(@RequestBody @Valid UserCreationRequest request) {
//		return ApiResponse.<UserResponse>builder().result(userService.createUserWithDoctorRole(request)).build();
//	}

	// Get Paged User with Profile
	@GetMapping
	ApiResponse<PageDataUserResponse> getPagedUsersWithProfile(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "username") String sortBy, @RequestParam(defaultValue = "asc") String order) {
		
		log.info("in controller sortBy: {}", sortBy);
		
		return ApiResponse.<PageDataUserResponse>builder().result(userService.getUsersWithProfile(page - 1, sortBy, order))
				.build();
	}

	@GetMapping("/my-info")
	ApiResponse<UserResponse> getMyInfo() {
		log.info("access to controller myinfo");
		return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
	}

	@PutMapping("/{userId}")
	ApiResponse<UserResponse> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateRequest request) {
		return ApiResponse.<UserResponse>builder().result(userService.updateUser(userId, request)).build();
	}

	@DeleteMapping("/{userId}")
	ApiResponse<String> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ApiResponse.<String>builder().result("User has been deleted").build();
	}
}
