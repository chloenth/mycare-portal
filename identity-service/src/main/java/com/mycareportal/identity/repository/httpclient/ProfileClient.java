package com.mycareportal.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.mycareportal.identity.configuration.FeignClientConfig;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = FeignClientConfig.class) // Attach
																													// the
																													// interceptor)
public interface ProfileClient {
	@PostMapping(value = "/internal/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<ProfileResponse> createProfile(@RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
			@RequestPart("profile") String profileRequestJson);

//	@GetMapping(value = "/internal/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	ApiResponse<ProfileResponse> getProfileByUserId(@PathVariable Long userId);

	@DeleteMapping(value = "/{profileId}")
	ApiResponse<Void> deleteProfile(@PathVariable String profileId);
}
