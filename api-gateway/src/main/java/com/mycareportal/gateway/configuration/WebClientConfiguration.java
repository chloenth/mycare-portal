package com.mycareportal.gateway.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.mycareportal.gateway.repository.IdentityClient;

@Configuration
public class WebClientConfiguration {
	@Bean
	WebClient webClient() {
		return WebClient.builder().baseUrl("http://localhost:8080/identity").build();
	}

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
		corsConfiguration.setAllowedHeaders(List.of("*"));
		corsConfiguration.setAllowedMethods(List.of("*"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setExposedHeaders(List.of("Set-Cookie")); // Allows frontend to read cookies

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

		return new CorsWebFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	IdentityClient identityClient(WebClient webClient) {
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
				.builderFor(WebClientAdapter.create(webClient)).build();
		return httpServiceProxyFactory.createClient(IdentityClient.class);
	}
}
