package com.mycareportal.identity.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.identity.dto.request.authentication.AuthenticationRequest;
import com.mycareportal.identity.dto.request.authentication.IntrospectRequest;
import com.mycareportal.identity.dto.request.authentication.LogoutRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.authentication.AuthenticationResponse;
import com.mycareportal.identity.dto.response.authentication.IntrospectResponse;
import com.mycareportal.identity.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
	ApiResponse<Void> authenticateUser(@RequestBody AuthenticationRequest request, HttpServletResponse httpResponse) {
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

		return ApiResponse.<Void>builder().build();
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspectAccessToken(@RequestBody IntrospectRequest request) {
		return ApiResponse.<IntrospectResponse>builder().result(authenticationService.introspect(request)).build();
	}

	@PostMapping("/refresh")
	ApiResponse<AuthenticationResponse> refreshToken(@CookieValue(name = "refreshToken") String refreshToken,
			HttpServletResponse httpResponse) {

		log.info("refreshToken: {}", refreshToken);

		AuthenticationResponse authResponse = authenticationService.refreshToken(refreshToken);

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

	@GetMapping("/logout")
	ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse httpResponse) {
		// Create a cookie to remove the token
		Cookie accessTokenCookie = new Cookie("accessToken", null); // "token" is the name of the cookie
		accessTokenCookie.setPath("/"); // Set the path to the root so the cookie is valid across the whole site
		accessTokenCookie.setHttpOnly(true); // Make the cookie inaccessible to JavaScript (security)
		accessTokenCookie.setSecure(true); // Only send over HTTPS (optional but recommended)
		accessTokenCookie.setMaxAge(0); // Set max age to 0 to expire the cookie immediately
		accessTokenCookie.setDomain("localhost"); // Set the domain if necessary (optional)

		// Add the cookie to the response
		httpResponse.addHeader("Set-Cookie",
				"accessToken=" + accessTokenCookie.getValue() + "; HttpOnly; Secure; SameSite=None; Path=/");
		httpResponse.addCookie(accessTokenCookie);

		// Get all cookies from the request
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// Check if the cookie's name is "token"
				if ("refreshToken".equals(cookie.getName())) {
					authenticationService.logout(LogoutRequest.builder().refreshToken(cookie.getValue()).build());

				}
			}
		}

		return ApiResponse.<Void>builder().build();
	}
}
