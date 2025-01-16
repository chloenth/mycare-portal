package com.mycareportal.profile.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mycareportal.profile.dto.request.ProfileCreationRequest;
import com.mycareportal.profile.dto.response.UserProfileResponse;
import com.mycareportal.profile.entity.UserProfile;
import com.mycareportal.profile.mapper.UserProfileMapper;
import com.mycareportal.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse getProfile(String profileId) {
        return userProfileRepository
                .findById(profileId)
                .map(userProfileMapper::toUserProfileResponse)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void deleteProfile(String profileId) {
        userProfileRepository.deleteById(profileId);
    }
}
