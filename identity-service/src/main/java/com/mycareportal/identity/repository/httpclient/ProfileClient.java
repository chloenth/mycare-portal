package com.mycareportal.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycareportal.identity.dto.request.ProfileCreationRequest;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profile")
public interface ProfileClient {
	@PostMapping(value="/internal/users",produces = MediaType.APPLICATION_JSON_VALUE)
	Object createProfile(@RequestBody ProfileCreationRequest request);
}
