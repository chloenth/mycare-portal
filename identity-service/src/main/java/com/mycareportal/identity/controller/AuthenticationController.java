package com.mycareportal.identity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.identity.dto.request.authentication.AuthenticationRequest;
import com.mycareportal.identity.dto.request.authentication.IntrospectRequest;
import com.mycareportal.identity.dto.request.authentication.LogoutRequest;
import com.mycareportal.identity.dto.request.authentication.RefreshRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.authentication.AuthenticationResponse;
import com.mycareportal.identity.dto.response.authentication.IntrospectResponse;
import com.mycareportal.identity.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/token")
	ApiResponse<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request,
			HttpServletResponse httpResponse) {
		AuthenticationResponse authResponse = authenticationService.authenticate(request);

		Cookie accessTokenCookie = new Cookie("accessToken", authResponse.getAccessToken());
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setSecure(true); // Only sent over HTTPS
		accessTokenCookie.setMaxAge(3600); // 1 hour expiry
		accessTokenCookie.setPath("/"); // Scope of the cookie

		// Manually add SameSite=Strict in the Set-Cookie header
		httpResponse.addHeader("Set-Cookie",
				"accessToken=" + accessTokenCookie.getValue() + "; HttpOnly; Secure; SameSite=None; Path=/");

		Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true); // Only sent over HTTPS
		refreshTokenCookie.setMaxAge(86400); // 1 day expiry
		refreshTokenCookie.setPath("/"); // Scope of the cookie

		// Manually add SameSite=Strict in the Set-Cookie header
		httpResponse.addHeader("Set-Cookie",
				"refreshToken=" + refreshTokenCookie.getValue() + "; HttpOnly; Secure; SameSite=None; Path=/");

		// Add cookies to the response
		httpResponse.addCookie(accessTokenCookie);
		httpResponse.addCookie(refreshTokenCookie);

		return ApiResponse.<AuthenticationResponse>builder().result(authResponse).build();
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspectAccessToken(@RequestBody IntrospectRequest request) {
		return ApiResponse.<IntrospectResponse>builder().result(authenticationService.introspect(request)).build();
	}

	@PostMapping("/refresh")
	ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) {
		return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.refreshToken(request))
				.build();
	}

	@PostMapping("/logout")
	ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
		authenticationService.logout(request);
		return ApiResponse.<Void>builder().build();
	}
}
