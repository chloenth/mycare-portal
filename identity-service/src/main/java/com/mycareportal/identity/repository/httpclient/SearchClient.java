package com.mycareportal.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycareportal.identity.dto.request.userprofile.UserProfileRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.userprofile.UserProfileResponse;

@FeignClient(name = "search-service", url = "${app.services.search}")

public interface SearchClient {
	@PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<UserProfileResponse> createUserProfile(UserProfileRequest request);

}
