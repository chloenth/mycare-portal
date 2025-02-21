package com.mycareportal.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycareportal.profile.dto.request.ProfileCreationRequest;
import com.mycareportal.profile.dto.response.ProfileResponse;
import com.mycareportal.profile.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    Profile toProfile(ProfileCreationRequest request);

    @Mapping(target = "avatar", ignore = true)
    ProfileResponse toProfileResponse(Profile profile);
}
