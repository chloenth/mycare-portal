package com.mycareportal.search.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.search.dto.request.UserProfileRequest;
import com.mycareportal.search.dto.response.api.ApiResponse;
import com.mycareportal.search.dto.response.userprofile.UserProfileResponse;
import com.mycareportal.search.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalSearchController {
	SearchService searchService;

//	@PostMapping
//	ApiResponse<UserProfileResponse> createUser(@RequestPart("user") UserProfileRequest request) {
//
//		return ApiResponse.<UserProfileResponse>builder().result(searchService.createUserProfile(request)).build();
//	}
}
