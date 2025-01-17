package com.mycareportal.gateway.service;

import org.springframework.stereotype.Service;

import com.mycareportal.gateway.dto.ApiResponse;
import com.mycareportal.gateway.dto.request.IntrospectRequest;
import com.mycareportal.gateway.dto.response.IntrospectResponse;
import com.mycareportal.gateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
	IdentityClient identityClient;
	
	public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
		return identityClient.introspect(IntrospectRequest.builder().token(token).build());
	}
}
