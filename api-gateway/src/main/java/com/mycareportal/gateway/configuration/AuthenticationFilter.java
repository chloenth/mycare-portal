package com.mycareportal.gateway.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycareportal.gateway.dto.ApiResponse;
import com.mycareportal.gateway.service.IdentityService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

	@NonFinal
	private String[] publicEndpoints = { "/identity/auth/.*", "/identity/users/registration", "/identity/kafka/send",
			"/search/kafka/messages" };

	@NonFinal
	@Value("${app.api-prefix}")
	private String apiPrefix;

	ObjectMapper objectMapper;
	IdentityService identityService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Enter authentication filter....");

		// if request path is public endpoint, continue the request
		if (isPublicEndpoint(exchange.getRequest())) {
			log.info("Enter authentication filter, public endpoint....");
			return chain.filter(exchange);
		}

		log.info("Enter authentication filter, private endpoint....");

		HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst("accessToken"); // Get the first
																									// matching cookie
		if (accessTokenCookie == null) {
			return unauthenticated(exchange.getResponse());
		}

		String token = accessTokenCookie.getValue();
		log.info("accessToken: {}", token);

		return identityService.introspect(token).flatMap(apiResponse -> {
			if (apiResponse.getResult().isValid()) {
				// Modify request by adding token to Authorization header
				ServerWebExchange modifiedExchange = exchange.mutate()
						.request(builder -> builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).build();

				return chain.filter(modifiedExchange);
			}
			return unauthenticated(exchange.getResponse());
		}).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
	}

	@Override
	public int getOrder() {

		return -1;
	}

	private boolean isPublicEndpoint(ServerHttpRequest request) {
		return Arrays.stream(publicEndpoints).anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
	}

	Mono<Void> unauthenticated(ServerHttpResponse response) {
		String body = null;

		ApiResponse<?> apiResponse = ApiResponse.builder().code(1401).message("Unauthenticated").build();

		try {
			body = objectMapper.writeValueAsString(apiResponse);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());

		return response.writeWith(Mono.just(buffer));
	}
}
