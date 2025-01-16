package com.mycareportal.identity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.identity.dto.ApiResponse;
import com.mycareportal.identity.dto.request.AuthenticationRequest;
import com.mycareportal.identity.dto.request.IntrospectRequest;
import com.mycareportal.identity.dto.request.RefreshRequest;
import com.mycareportal.identity.dto.response.AuthenticationResponse;
import com.mycareportal.identity.dto.response.IntrospectResponse;
import com.mycareportal.identity.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/token")
	ApiResponse<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
		return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.authenticate(request))
				.build();
	}
	
	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspectAccessToken(@RequestBody IntrospectRequest request) {
		return ApiResponse.<IntrospectResponse>builder().result(authenticationService.introspect(request)).build();
	}
	
	@PostMapping("/refresh")
	ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) {
		return ApiResponse.<AuthenticationResponse>builder()
				.result(authenticationService.refreshToken(request)).build();
	}
}
