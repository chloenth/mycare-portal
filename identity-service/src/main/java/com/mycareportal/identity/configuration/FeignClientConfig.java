package com.mycareportal.identity.configuration;

import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FeignClientConfig {
	// Define a list of endpoints that DO NOT require a token (wildcard converted to
	// regex)
	private static final List<String> NO_AUTH_ENDPOINT_PATTERNS = List.of("^/internal/users(/.*)?$" // Matches
																									// /internal/users
																									// and
																									// /internal/users/{id}
	);

	@Bean
	RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			String relativeUrl = requestTemplate.url();

			// Check if the request path matches any NO_AUTH endpoint pattern
			if (NO_AUTH_ENDPOINT_PATTERNS.stream().anyMatch(relativeUrl::matches)) {
				return; // Do not add Authorization header
			}

			// Get the current authentication context
			var authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof JwtAuthenticationToken jwtauthenticationtoken) {
				Jwt jwt = jwtauthenticationtoken.getToken();
				requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
			}
		};
	}

}
