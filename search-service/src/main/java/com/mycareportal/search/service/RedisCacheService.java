package com.mycareportal.search.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.mycareportal.search.dto.response.userprofile.UserProfileResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class RedisCacheService {
	RedisTemplate<String, Object> redisTemplate;

	static String LOGGED_IN_USER_KEY = "logged_in_user_profile:";
	Duration tokenTTL = Duration.ofHours(1);

	// Lưu thông tin người dùng vào Redis
	public void saveUserProfileToCache(String username, UserProfileResponse userProfile) {
		redisTemplate.opsForValue().set(LOGGED_IN_USER_KEY + username, userProfile, tokenTTL);
	}

	// Lấy thông tin người dùng từ Redis
	public UserProfileResponse getUserProfileFromCache(String username) {
		Object userProfile = redisTemplate.opsForValue().get(LOGGED_IN_USER_KEY + username);
		return (userProfile != null) ? (UserProfileResponse) userProfile : null;
	}

	// Kiểm tra xem thông tin người dùng có trong Redis hay không
	public boolean isUserProfileInCache(String username) {
		return Optional.ofNullable(redisTemplate).map(template -> template.hasKey(LOGGED_IN_USER_KEY + username))
				.orElse(false);
	}
}
