package com.mycareportal.search.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.event.KafkaMessage;
import com.mycareportal.search.dto.request.UserProfileRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/kafka")
@Slf4j
public class KafkaConsumerController {

	private final List<Object> receivedMessages = new ArrayList<>();

	@KafkaListener(topics = "user-profile-created-topic", groupId = "search-service-group")
	public void listen(ConsumerRecord<String, KafkaMessage<UserProfileRequest>> consumerRecord) {
		KafkaMessage<UserProfileRequest> message = consumerRecord.value();
		receivedMessages.add(message);
		log.info("payload in consumer controller: {}", message.getPayload());

	}

	@GetMapping("/messages")
	public ResponseEntity<List<Object>> getMessages() {
		return ResponseEntity.ok(receivedMessages);
	}
}
