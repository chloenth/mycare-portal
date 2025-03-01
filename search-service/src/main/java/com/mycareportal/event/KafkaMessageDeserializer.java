package com.mycareportal.event;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaMessageDeserializer<T> implements Deserializer<KafkaMessage<T>> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private Class<T> targetType;

	// Add a public no-argument constructor
	public KafkaMessageDeserializer() {
		objectMapper.registerModule(new JavaTimeModule()); // Support LocalDate
	}

	// Allow configuring targetType dynamically (set from Kafka consumer config)
//	@Override
//	public void configure(Map<String, ?> configs, boolean isKey) {
////		String className = (String) configs.get("kafka.deserializer.targetType");
////
////		try {
////			this.targetType = (Class<T>) Class.forName(className);
////
////		} catch (ClassNotFoundException e) {
////			throw new RuntimeException("Could not find class for Kafka deserialization: " + className, e);
////		}
//	}

	@SuppressWarnings("unchecked")
	@Override
	public KafkaMessage<T> deserialize(String topic, byte[] data) {
		log.info("Start deserializing topic: {}", topic);

		String className = "com.mycareportal.search.dto.request.UserProfileRequest";

		if ("username-updated-topic".equals(topic)) {
			className = "com.mycareportal.search.dto.request.UsernameUpdateRequest";
			
		} else if ("profile-updated-topic".equals(topic)) {
			className = "com.mycareportal.search.dto.request.ProfileUpdateRequest";
			
		}

		try {
			// Log the raw data to inspect it
			log.info("Raw data: {}", new String(data));

			this.targetType = (Class<T>) Class.forName(className);

			// Use JavaType to correctly handle the generic type
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(KafkaMessage.class, targetType);

			// Deserialize data to KafkaMessage<UserProfileRequest>
			KafkaMessage<T> message = objectMapper.readValue(data, javaType);

			log.info("type: {}", message.getType());

			log.info("payload: {}", message.getPayload());

			// Return the KafkaMessage with the correct payload type
			return message;

		} catch (IOException | ClassNotFoundException e) {
			log.error("Error deserializing KafkaMessage", e);
			throw new RuntimeException("Error deserializing KafkaMessage from topic: " + topic, e);
		}

	}

	@Override
	public void close() {
		// Cleanup if needed
	}

}
