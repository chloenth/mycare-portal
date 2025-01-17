package com.mycareportal.gateway.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.mycareportal.gateway.dto.ApiResponse;
import com.mycareportal.gateway.dto.request.IntrospectRequest;
import com.mycareportal.gateway.dto.response.IntrospectResponse;

import reactor.core.publisher.Mono;

public interface IdentityClient {
	@PostExchange(url="/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
	Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
