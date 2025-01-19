package com.mycareportal.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycareportal.identity.dto.ApiResponse;
import com.mycareportal.identity.dto.request.ProfileCreationRequest;
import com.mycareportal.identity.dto.response.UserProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
	@PostMapping(value="/internal/users",produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
	
	@DeleteMapping(value="/internal/users/{profileId}")
	ApiResponse<Void> deleteProfile(@PathVariable String profileId);
}
