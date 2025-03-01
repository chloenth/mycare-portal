package com.mycareportal.event.dto;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaMessageSerializer<T> implements Serializer<KafkaMessage<T>> {

	private final ObjectMapper objectMapper;

	// Constructor initializes the ObjectMapper only once
	public KafkaMessageSerializer() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // Register module once
	}

	@Override
	public byte[] serialize(String topic, KafkaMessage<T> data) {
		try {
			log.info("data in KafkaMessageSerializer: {}", data);
			return objectMapper.writeValueAsBytes(data);
			
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error serializing KafkaMessage", e);
		}
	}
}
