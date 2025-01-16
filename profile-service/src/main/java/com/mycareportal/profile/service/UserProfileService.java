package com.mycareportal.profile.service;

import org.springframework.stereotype.Service;

import com.mycareportal.profile.dto.request.ProfileCreationRequest;
import com.mycareportal.profile.dto.response.UserProfileResponse;
import com.mycareportal.profile.entity.UserProfile;
import com.mycareportal.profile.mapper.UserProfileMapper;
import com.mycareportal.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }
}
