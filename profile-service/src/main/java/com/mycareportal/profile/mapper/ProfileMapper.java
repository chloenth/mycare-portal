package com.mycareportal.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mycareportal.profile.dto.request.KafkaProfileUpdateRequest;
import com.mycareportal.profile.dto.request.ProfileCreationRequest;
import com.mycareportal.profile.dto.request.ProfileUpdateRequest;
import com.mycareportal.profile.dto.response.ProfileResponse;
import com.mycareportal.profile.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    Profile toProfile(ProfileCreationRequest request);

    @Mapping(target = "avatar", ignore = true)
    ProfileResponse toProfileResponse(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    Profile toUpdateProfile(@MappingTarget Profile profile, ProfileUpdateRequest request);

    KafkaProfileUpdateRequest toKafkaProfileUpdateRequest(ProfileResponse response);
}
