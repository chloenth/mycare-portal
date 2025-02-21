package com.mycareportal.gateway.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TrimRequestBodyFilter implements GlobalFilter, Ordered {

	private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON processing

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// Only apply filter for POST requests
		if (exchange.getRequest().getMethod() != HttpMethod.POST) {
			return chain.filter(exchange);
		}

		// Only process JSON requests
		if (!MediaType.APPLICATION_JSON.isCompatibleWith(exchange.getRequest().getHeaders().getContentType())) {
			return chain.filter(exchange);
		}

		return DataBufferUtils.join(exchange.getRequest().getBody()) // Read request body
				.flatMap(dataBuffer -> {
					byte[] bytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(bytes);
					DataBufferUtils.release(dataBuffer);

					String body = new String(bytes, StandardCharsets.UTF_8);

					try {
						// Convert JSON string to Map
						Map<String, Object> requestBody = objectMapper.readValue(body, Map.class);

						// Trim all string values
						requestBody.replaceAll(
								(key, value) -> (value instanceof String) ? ((String) value).trim() : value);

						// Convert modified Map back to JSON string
						String updatedBody = objectMapper.writeValueAsString(requestBody);
						byte[] updatedBodyBytes = updatedBody.getBytes(StandardCharsets.UTF_8);

						// Create a new DataBuffer
						DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(updatedBodyBytes);

						// Create a decorated request with trimmed body
						ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
								.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(updatedBodyBytes.length)).build();

						ServerHttpRequestDecorator decoratedRequest = new ServerHttpRequestDecorator(mutatedRequest) {
							@Override
							public Flux<DataBuffer> getBody() {
								return Flux.just(buffer); // Return a Flux instead of Mono
							}
						};

						return chain.filter(exchange.mutate().request(decoratedRequest).build());
					} catch (Exception e) {
						return Mono.error(e);
					}
				});
	}

	@Override
	public int getOrder() {
		return -2; // High priority to process request first
	}
}
