package com.mycareportal.profile.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycareportal.profile.dto.ApiResponse;
import com.mycareportal.profile.dto.response.ProfileResponse;
import com.mycareportal.profile.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalProfileController {
    ProfileService userProfileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    ApiResponse<ProfileResponse> createProfile(
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestPart("profile") String profileRequestJson) {

        return ApiResponse.<ProfileResponse>builder()
                .result(userProfileService.createProfile(avatarFile, profileRequestJson))
                .build();
    }

    @DeleteMapping("/{profileId}")
    ApiResponse<Void> deleteProfile(@PathVariable Long profileId) {
        userProfileService.deleteProfile(profileId);
        return ApiResponse.<Void>builder().message("Profile has been deleted").build();
    }
}
