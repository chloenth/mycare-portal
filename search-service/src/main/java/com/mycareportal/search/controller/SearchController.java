package com.mycareportal.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.search.dto.response.api.ApiResponse;
import com.mycareportal.search.dto.response.userprofile.PageUserProfileResponse;
import com.mycareportal.search.dto.response.userprofile.UserProfileResponse;
import com.mycareportal.search.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchController {
	SearchService searchService;

	@GetMapping
	public ApiResponse<PageUserProfileResponse> searchUsers(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "username") String sortBy,
			@RequestParam(defaultValue = "asc") String order, @RequestParam(required = false) String searchText,
			@RequestParam(defaultValue = "all") String gender, @RequestParam(defaultValue = "all") String role) {

		return ApiResponse.<PageUserProfileResponse>builder()
				.result(searchService.searchUser(page - 1, size, sortBy, order, searchText, gender, role)).build();
	}

	@GetMapping("/{userId}")
	public ApiResponse<UserProfileResponse> getUserById(@PathVariable Long userId) {
		return ApiResponse.<UserProfileResponse>builder().result(searchService.getUserById(userId)).build();
	}

	@GetMapping("/my-info")
	public ApiResponse<UserProfileResponse> getUserById() {
		return ApiResponse.<UserProfileResponse>builder().result(searchService.getMyInfo()).build();
	}

}
