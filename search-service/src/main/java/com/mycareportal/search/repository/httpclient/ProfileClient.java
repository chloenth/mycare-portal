package com.mycareportal.search.repository.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.mycareportal.search.dto.response.api.ApiResponse;
import com.mycareportal.search.dto.response.profile.ProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
	
	@GetMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<List<ProfileResponse>> getAllProfiles();
}
