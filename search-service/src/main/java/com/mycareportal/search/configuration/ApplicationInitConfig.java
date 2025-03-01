package com.mycareportal.search.configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycareportal.search.dto.response.profile.ProfileResponse;
import com.mycareportal.search.dto.response.user.UserResponse;
import com.mycareportal.search.entity.UserProfileIndex;
import com.mycareportal.search.mapper.UserProfileMapper;
import com.mycareportal.search.repository.UserProfileRepository;
import com.mycareportal.search.repository.httpclient.IdentityClient;
import com.mycareportal.search.repository.httpclient.ProfileClient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Configuration
public class ApplicationInitConfig {
	IdentityClient identityClient;
	ProfileClient profileClient;

	ElasticsearchClient client;
	UserProfileRepository userProfileRepository;

	UserProfileMapper userProfileMapper;

	@NonFinal
	static final String INDEX_NAME = "user-profile";

	@Bean
	ApplicationRunner applicationRunner() {
		log.info("Initializing application.....");
		return args -> {
			boolean isIndexEmpty = isIndexEmpty();

			if (isIndexEmpty) {
				log.info("Elasticsearch index is empty. Fetching and indexing users...");

				fetchAndIndexUsersAsync();

				log.info("User data successfully indexed into Elasticsearch.");

			} else {
				log.info("Elasticsearch index already contains data. Skipping indexing.");

			}

			log.info("Application initialization completed .....");
		};

	}

	private boolean isIndexEmpty() {
		try {
			CountRequest countRequest = CountRequest.of(req -> req.index(INDEX_NAME));
			CountResponse countResponse = client.count(countRequest);

			long docCount = countResponse.count();
			log.info("Elasticsearch index '{}' contains {} documents.", INDEX_NAME, docCount);

			return docCount == 0; // If zero documents, return true (index is empty)
		} catch (Exception e) {
			log.error("Error checking Elasticsearch index: {}", e.getMessage());
			return true; // Assume empty if an error occurs
		}
	}

	private void fetchAndIndexUsersAsync() {
		// Example of parallel fetching users and profiles
		CompletableFuture<List<UserResponse>> usersFuture = CompletableFuture
				.supplyAsync(() -> identityClient.getAllUsers().getResult());

		CompletableFuture<List<ProfileResponse>> profilesFuture = CompletableFuture
				.supplyAsync(() -> profileClient.getAllProfiles().getResult());

		CompletableFuture.allOf(usersFuture, profilesFuture).join();

		try {
			List<UserResponse> users = usersFuture.get();
			List<ProfileResponse> profiles = profilesFuture.get();

			Map<Long, ProfileResponse> profileMap = profiles.stream()
					.collect(Collectors.toMap(ProfileResponse::getUserId, Function.identity()));

			List<UserProfileIndex> userProfiles = users.stream().map(user -> {
				ProfileResponse profile = profileMap.get(user.getId());

				if (profile != null) {
					UserProfileIndex index = userProfileMapper.toUserProfileIndex(user, profile);
					index.setDobToLong(profile.getDob());

					return index;
				} else {
					log.warn("Profile data missing for user {}", user.getId());

					return userProfileMapper.toUserProfileIndex(user, profile); // Handle missing profiles accordingly
				}

			}).toList();

			userProfileRepository.saveAll(userProfiles);

		} catch (Exception e) {
			log.error("Error fetching and indexing users: {}", e.getMessage());
		}
	}
}
