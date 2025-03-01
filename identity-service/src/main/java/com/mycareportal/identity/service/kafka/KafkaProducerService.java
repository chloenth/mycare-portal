package com.mycareportal.identity.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.mycareportal.event.dto.KafkaMessage;
import com.mycareportal.identity.dto.request.kafka.UsernameKafkaUpdateRequest;
import com.mycareportal.identity.dto.request.userprofile.UserProfileRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KafkaProducerService {
	KafkaTemplate<String, Object> kafkaTemplate;

	@NonFinal
	@Value("${spring.kafka.topic.user-profile-created}")
	String userProfileCreatedTopic;

	@NonFinal
	@Value("${spring.kafka.topic.username-updated}")
	String usernameUpdatedTopic; // Inject topic name from application.yml

	public void sendUserCreatedEvent(KafkaMessage<UserProfileRequest> event) {
		try {
			log.info("hello from producer");
			log.info("event payload avatar in producer service: {}", event.getPayload().getAvatar());
			kafkaTemplate.send(userProfileCreatedTopic, event).get();
			log.info("Message sent sucessfully");

		} catch (Exception e) {
			log.error("Failed to send message, saving to DLQ", e);
			kafkaTemplate.send("user-profile-created-topic-dlq", event); // Gửi
			throw new RuntimeException(e);
		}
	}

	public void sendUsernameUpdatedEvent(KafkaMessage<UsernameKafkaUpdateRequest> event) {
		try {
			log.info("hello from producer");
			log.info("event payload username in producer service: {}", event.getPayload().getUsername());
			kafkaTemplate.send(usernameUpdatedTopic, event).get();
			log.info("Message sent sucessfully");

		} catch (Exception e) {
			log.error("Failed to send message, saving to DLQ", e);
			throw new RuntimeException(e);
		}
	}
}
