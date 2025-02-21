package com.mycareportal.identity.repository.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.mycareportal.identity.configuration.FeignClientConfig;
import com.mycareportal.identity.dto.request.profile.ProfileCreationRequest;
import com.mycareportal.identity.dto.response.api.ApiResponse;
import com.mycareportal.identity.dto.response.pagedata.profile.PageDataProfileResponse;
import com.mycareportal.identity.dto.response.pagedata.profile.ProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = FeignClientConfig.class) // Attach
																													// the
																													// interceptor)
public interface ProfileClient {
	@PostMapping(value = "/internal/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<ProfileResponse> createProfile(@RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
			@RequestPart("profile") String profileRequestJson);

	@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<PageDataProfileResponse> getProfiles(@RequestParam(required = false) List<Long> userIds,  @RequestParam(required = false) int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order);

	@DeleteMapping(value = "/{profileId}")
	ApiResponse<Void> deleteProfile(@PathVariable String profileId);
}
