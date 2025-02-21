package com.mycareportal.profile.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.profile.dto.ApiResponse;
import com.mycareportal.profile.dto.response.PageDataProfileResponse;
import com.mycareportal.profile.dto.response.ProfileResponse;
import com.mycareportal.profile.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileController {
    ProfileService userProfileService;

    @GetMapping
    ApiResponse<PageDataProfileResponse> getProfiles(
            @RequestParam(required = false) List<Long> userIds,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        log.info("userIds: {}", userIds);

        log.info("in profile controller pages: {}", page);

        return ApiResponse.<PageDataProfileResponse>builder()
                .result(userProfileService.getProfiles(userIds, page, sortBy, order))
                .build();
    }

    @GetMapping("/{profileId}")
    ApiResponse<ProfileResponse> getProfile(@PathVariable Long profileId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(userProfileService.getProfile(profileId))
                .build();
    }
}
