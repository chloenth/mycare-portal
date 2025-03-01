package com.mycareportal.profile.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycareportal.event.dto.KafkaMessage;
import com.mycareportal.profile.dto.request.KafkaProfileUpdateRequest;
import com.mycareportal.profile.dto.request.ProfileCreationRequest;
import com.mycareportal.profile.dto.request.ProfileUpdateRequest;
import com.mycareportal.profile.dto.response.PageDataProfileResponse;
import com.mycareportal.profile.dto.response.PageDataResponse;
import com.mycareportal.profile.dto.response.ProfileResponse;
import com.mycareportal.profile.entity.Profile;
import com.mycareportal.profile.exception.AppException;
import com.mycareportal.profile.exception.ErrorCode;
import com.mycareportal.profile.mapper.ProfileMapper;
import com.mycareportal.profile.repository.ProfileRepository;
import com.mycareportal.profile.service.kafka.KafkaProducerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    ObjectMapper objectMapper;

    KafkaProducerService kafkaProducerService;

    @PersistenceContext
    private EntityManager entityManager;

    int PAGE_SIZE = 5;
    String ASCENDING = "asc";

    // Create New profile
    public ProfileResponse createProfile(MultipartFile avatarFile, String profileRequestJson) {
        ProfileCreationRequest profileRequest;

        try {
            profileRequest = objectMapper.readValue(profileRequestJson, ProfileCreationRequest.class);

            log.info("phoneNumber in profile service: {}", profileRequest.getPhoneNumber());

            // Convert the profileRequestJson to class ProfileCreationRequest
            Profile profile = profileMapper.toProfile(profileRequest);

            if (avatarFile != null) {
                profile.setAvatar(avatarFile.getBytes());
            }

            profile = profileRepository.save(profile);

            byte[] avatarBytes = profile.getAvatar();
            String avatarBase64 = (avatarBytes != null) ? Base64.getEncoder().encodeToString(avatarBytes) : null;

            ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
            profileResponse.setAvatar(avatarBase64);

            return profileResponse;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Get All Profiles
    public List<ProfileResponse> getAllProfiles() {

        return profileRepository.findAll().stream()
                .map(profile -> {
                    ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);

                    byte[] avatarBytes = profile.getAvatar();

                    String avatarBase64 =
                            (avatarBytes != null) ? Base64.getEncoder().encodeToString(avatarBytes) : null;
                    profileResponse.setAvatar(avatarBase64);

                    return profileResponse;
                })
                .toList();
    }

    // Get Profiles
    @PreAuthorize("hasRole('ADMIN')")
    public PageDataProfileResponse getProfiles(List<Long> userIds, int page, String sortBy, String order) {
        // if no sort field, then get the profiles by userIds
        if (userIds != null && "".equals(sortBy)) {
            List<Profile> profiles = profileRepository.findAllByUserIdIn(userIds);

            List<ProfileResponse> profilesResponse = profiles.stream()
                    .map(profile -> {
                        byte[] avatarBytes = profile.getAvatar();
                        String avatarBase64 =
                                (avatarBytes != null) ? Base64.getEncoder().encodeToString(profile.getAvatar()) : null;

                        ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
                        profileResponse.setAvatar(avatarBase64);

                        return profileResponse;
                    })
                    .toList();

            return new PageDataProfileResponse(profilesResponse, null);
        }

        // if there is sort field

        return getUsersAndSortBy(page, sortBy, order);
    }

    public ProfileResponse getProfileById(Long profileId) {
        return profileRepository
                .findById(profileId)
                .map(profileMapper::toProfileResponse)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
    }

    // Update Profile
    public ProfileResponse updateProfile(Long profileId, ProfileUpdateRequest request, MultipartFile avatarFile) {
        var profile =
                profileRepository.findById(profileId).orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        profile = profileMapper.toUpdateProfile(profile, request);

        try {
            if (avatarFile != null) {
                profile.setAvatar(avatarFile.getBytes());
            }

            profile = profileRepository.save(profile);

            byte[] avatarBytes = profile.getAvatar();
            String avatarBase64 = (avatarBytes != null) ? Base64.getEncoder().encodeToString(avatarBytes) : null;

            ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
            profileResponse.setAvatar(avatarBase64);

            KafkaProfileUpdateRequest kafkaRequest = profileMapper.toKafkaProfileUpdateRequest(profileResponse);

            kafkaProducerService.sendProfileUpdatedEvent(KafkaMessage.<KafkaProfileUpdateRequest>builder()
                    .type("ProfileUpdate")
                    .payload(kafkaRequest)
                    .build());

            return profileResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Delete Profile
    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    private PageDataProfileResponse getUsersAndSortBy(int page, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Profile> pageData = findAllSortedByField(sortBy, order, pageable);

        List<Profile> profiles = pageData.getContent();

        List<ProfileResponse> profilesResponse = profiles.stream()
                .map(profile -> {
                    log.info("profile gender: {}", profile.getGender());

                    byte[] avatarBytes = profile.getAvatar();
                    String avatarBase64 =
                            (avatarBytes != null) ? Base64.getEncoder().encodeToString(avatarBytes) : null;

                    ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
                    profileResponse.setAvatar(avatarBase64);

                    return profileResponse;
                })
                .toList();

        int currentPage = pageData.getNumber() + 1;
        int totalPages = pageData.getTotalPages();

        int startPage = Math.max(1, currentPage - 3);
        int endPage = Math.min(totalPages, startPage + 5);

        PageDataResponse pageDataResponse = new PageDataResponse(currentPage, totalPages, startPage, endPage);

        return new PageDataProfileResponse(profilesResponse, pageDataResponse);
    }

    // Custom query for sorting handle NULL ORDER by using
    // entityManager.createNativeQuery()
    public Page<Profile> findAllSortedByField(String sortBy, String order, Pageable pageable) {
        String nullSortingOrder = ASCENDING.equalsIgnoreCase(order) ? "NULLS FIRST" : "NULLS LAST";

        String query = "SELECT * FROM profile p ORDER BY " + sortBy + " " + order + " " + nullSortingOrder;
        Query nativeQuery = entityManager.createNativeQuery(query, Profile.class);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        nativeQuery.setFirstResult(pageNumber * pageSize);
        nativeQuery.setMaxResults(pageSize);

        // Get the total count of records (profiles)
        String countQuery = "SELECT COUNT(*) FROM profile p";
        Query countNativeQuery = entityManager.createNativeQuery(countQuery);
        long totalRecords = ((Number) countNativeQuery.getSingleResult()).longValue();

        List<Profile> results = nativeQuery.getResultList();

        return new PageImpl<>(results, pageable, totalRecords);
    }
}
