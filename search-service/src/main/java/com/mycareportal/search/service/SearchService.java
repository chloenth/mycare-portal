package com.mycareportal.search.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.event.KafkaMessage;
import com.mycareportal.search.dto.request.ProfileUpdateRequest;
import com.mycareportal.search.dto.request.UserProfileRequest;
import com.mycareportal.search.dto.request.UsernameUpdateRequest;
import com.mycareportal.search.dto.response.userprofile.PageDataResponse;
import com.mycareportal.search.dto.response.userprofile.PageUserProfileResponse;
import com.mycareportal.search.dto.response.userprofile.UserProfileResponse;
import com.mycareportal.search.entity.UserProfileIndex;
import com.mycareportal.search.exception.AppException;
import com.mycareportal.search.exception.ErrorCode;
import com.mycareportal.search.mapper.UserProfileMapper;
import com.mycareportal.search.repository.UserProfileRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class SearchService {
	UserProfileRepository userProfileRepository;
	ElasticsearchClient elasticsearchClient;

	UserProfileMapper userProfileMapper;

	@KafkaListener(topics = "${spring.kafka.topic.user-profile-created}", groupId = "${spring.kafka.consumer.group-id.user-created}")
	public void listenCreatedUserProfile(ConsumerRecord<String, KafkaMessage<UserProfileRequest>> consumerRecord,
			Acknowledgment acknowledgment) {
		try {
			KafkaMessage<UserProfileRequest> message = consumerRecord.value();
			log.info("payload in search service: {}", message.getPayload());

			UserProfileRequest userProfileRequest = message.getPayload();
			log.info("userProfileRequest in search service: {}", userProfileRequest);

			UserProfileIndex userProfileIndex = userProfileMapper.toUserProfileIndex(userProfileRequest);
			userProfileIndex.setDobToLong(userProfileRequest.getDob());

			UserProfileResponse userProfileResponse = userProfileMapper
					.toUserProfileResponse(userProfileRepository.save(userProfileIndex));

			log.info("userProfileResponse: {}", userProfileResponse);

			// Sau khi xử lý xong, commit thủ công offset
			acknowledgment.acknowledge();

		} catch (DataAccessException e) {
			// Xử lý lỗi khi lưu dữ liệu vào Elasticsearch
			log.error("Failed to save user profile to Elasticsearch", e);
			throw new RuntimeException("Could not create user profile", e);
		} catch (Exception e) {
			// Xử lý lỗi khác nếu có
			log.error("Error processing the Kafka message", e);
			throw new RuntimeException("Unexpected error", e);
		}
	}

	// Search Users with Profile
	@PreAuthorize("hasRole('ADMIN')")
	public PageUserProfileResponse searchUser(int page, int size, String sortBy, String order, String searchText,
			String gender, String role) {
		SearchRequest searchRequest = buildSearchRequest(page, size, sortBy, order, searchText, gender, role);

		// Execute the search query
		SearchResponse<Map> searchResponse = null;

		try {
			searchResponse = elasticsearchClient.search(searchRequest, Map.class);
			
			// Extract the hits (documents) from the response
			List<Map<String, Object>> searchResults = searchResponse.hits().hits().stream().map(hit -> {
				Map<String, Object> source = hit.source();
				if (source.containsKey("dob")) {
					Object dobValue = source.get("dob");

					// Nếu dob là Long (epoch millis) → Chuyển thành String format dd-MM-yyyy
					if (dobValue instanceof Long) {
						Instant instant = Instant.ofEpochMilli((Long) dobValue);
						LocalDate dob = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						String formattedDob = dob.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
						source.put("dob", formattedDob);
					}

				}
				return source; // Trả về Map đã được thay đổi
			}).toList(); // Get the source document
			// from each hit

			long totalRecords = searchResponse.hits().total().value();
			int currentPage = searchRequest.from() / size + 1;
			int totalPages = (int) Math.ceil((double) totalRecords / size);

			int startPage = Math.max(1, currentPage - 3);
			int endPage = Math.min(totalPages, startPage + 5);

			PageDataResponse pageDataResponse = PageDataResponse.builder().currentPage(currentPage)
					.totalPages(totalPages).startPage(startPage).endPage(endPage).totalRecords(totalRecords).build();

			return PageUserProfileResponse.builder().searchResults(searchResults).pageDataResponse(pageDataResponse)
					.build();

		} catch (ElasticsearchException | IOException e) {
			log.error("Error executing search query", e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private SearchRequest buildSearchRequest(int page, int size, String sortBy, String order, String searchText,
			String gender, String role) {
		// Create the sort field
		if (!"dob".equals(sortBy)) {
			sortBy = sortBy + ".keyword";
		}

		FieldSort sortField = new FieldSort.Builder().field(sortBy) // Replace with the actual field you
																	// want to sort by
				.order(order.equalsIgnoreCase("asc") ? SortOrder.Asc : SortOrder.Desc) // "asc" for ascending or "desc"
																						// for descending
				.missing(order.equalsIgnoreCase("asc") ? "_first" : "_last") // "_first" to place null values first in
																				// ascending order
				.build();

		// Initialize BoolQuery builder
		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

		// Add search text conditions
		if (searchText != null) {
			boolQueryBuilder.must(
					q -> q.bool(b -> b.should(s -> s.wildcard(w -> w.field("username").value("*" + searchText + "*")))
							.should(s -> s.wildcard(w -> w.field("fullName").value("*" + searchText + "*")))
							.should(s -> s.wildcard(w -> w.field("email").value("*" + searchText + "*")))
							.should(s -> s.wildcard(w -> w.field("phoneNumber").value("*" + searchText + "*")))
							.should(s -> s.wildcard(w -> w.field("address").value("*" + searchText + "*")))));
		}

		// Add filters for gender & role (only if not "all")
		if (!"all".equalsIgnoreCase(gender)) {
			boolQueryBuilder.must(q -> q.term(t -> t.field("gender").value(gender)));
		}

		if (!"all".equalsIgnoreCase(role)) {
			boolQueryBuilder.must(q -> q.nested(n -> n.path("roles") // Access the "roles" array
					.query(nq -> nq.term(t -> t.field("roles.name.keyword").value(role))) // Filter by role name
			));
		}

		// Build final BoolQuery
		BoolQuery boolQuery = boolQueryBuilder.build();

		// Return SearchRequest with BoolQuery
		return new SearchRequest.Builder().query(q -> q.bool(boolQuery))
				.sort(Collections.singletonList(SortOptions.of(s -> s.field(sortField)))).from(page * size).size(size)
				.build();
	}

	@KafkaListener(topics = "${spring.kafka.topic.username-updated}", groupId = "${spring.kafka.consumer.group-id.username-updated}")
	public void listenUpdatedUsername(ConsumerRecord<String, KafkaMessage<UsernameUpdateRequest>> consumerRecord,
			Acknowledgment acknowledgment) {
		try {
			KafkaMessage<UsernameUpdateRequest> message = consumerRecord.value();
			log.info("payload in search service: {}", message.getPayload());

			UsernameUpdateRequest usernameUpdateRequest = message.getPayload();
			log.info("userProfileRequest in search service: {}", usernameUpdateRequest);

			var index = userProfileRepository.findById(usernameUpdateRequest.getUserId())
					.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
			
			log.info("index: {index}", index);
			
			index.setUsername(usernameUpdateRequest.getUsername());

			updateUserProfile(index);

			// Sau khi xử lý xong, commit thủ công offset
			acknowledgment.acknowledge();

		} catch (DataAccessException e) {
			// Xử lý lỗi khi lưu dữ liệu vào Elasticsearch
			log.error("Failed to save user profile to Elasticsearch", e);
			throw new RuntimeException("Could not create user profile", e);
		} catch (Exception e) {
			// Xử lý lỗi khác nếu có
			log.error("Error processing the Kafka message", e);
			throw new RuntimeException("Unexpected error", e);
		}
	}
	
	@KafkaListener(topics = "${spring.kafka.topic.profile-updated}", groupId = "${spring.kafka.consumer.group-id.profile-updated}")
	public void listenUpdatedProfile(ConsumerRecord<String, KafkaMessage<ProfileUpdateRequest>> consumerRecord,
			Acknowledgment acknowledgment) {
		try {
			KafkaMessage<ProfileUpdateRequest> message = consumerRecord.value();
			log.info("payload in search service: {}", message.getPayload());

			ProfileUpdateRequest request = message.getPayload();
			log.info("userProfileRequest in search service: {}", request);

			UserProfileIndex index = userProfileRepository.findByProfileId(request.getId())
					.orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
			
			log.info("index: {index}", index);
			
			index = userProfileMapper.toUserProfileUpdateIndex(index, request);
			index.setDobToLong(request.getDob());

			updateUserProfile(index);

			// Sau khi xử lý xong, commit thủ công offset
			acknowledgment.acknowledge();

		} catch (DataAccessException e) {
			// Xử lý lỗi khi lưu dữ liệu vào Elasticsearch
			log.error("Failed to save user profile to Elasticsearch", e);
			throw new RuntimeException("Could not create user profile", e);
		} catch (Exception e) {
			// Xử lý lỗi khác nếu có
			log.error("Error processing the Kafka message", e);
			throw new RuntimeException("Unexpected error", e);
		}
	}

	@Retryable(retryFor = { DataAccessException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	public void updateUserProfile(UserProfileIndex index) {
		userProfileRepository.save(index);
	}
	
//	Get users By Id
	public UserProfileResponse getUserById(Long id) {
		var index = userProfileRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		
		UserProfileResponse userProfileResponse = userProfileMapper.toUserProfileResponse(index);
		userProfileResponse.setDob(index.getDobAsString());
		
		return userProfileResponse;
	}
}
