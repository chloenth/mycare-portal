package com.mycareportal.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.profile.dto.request.ProfileCreationRequest;
import com.mycareportal.profile.dto.response.UserProfileResponse;
import com.mycareportal.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(target = "id", ignore = true)
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
