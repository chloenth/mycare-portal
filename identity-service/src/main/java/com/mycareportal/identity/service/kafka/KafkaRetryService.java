package com.mycareportal.identity.service.kafka;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;

import com.mycareportal.event.dto.KafkaMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@EnableKafka
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KafkaRetryService {

	KafkaTemplate<String, KafkaMessage> kafkaTemplate;

	@KafkaListener(topics = "${spring.kafka.topic.dlq}", groupId = "retry-consumer-group")
	public void listenDlq(ConsumerRecord<String, KafkaMessage> message) {
		// Đọc message từ DLQ và gửi lại vào topic chính
		try {
			
			log.info("hello, iam in listenDlq");
			// Gửi lại vào topic chính và nhận CompletableFuture
			CompletableFuture<SendResult<String, KafkaMessage>> future = kafkaTemplate
					.send("user-profile-created-topic", message.value());

			// Sử dụng thenAccept để xử lý khi gửi thành công
			future.thenAccept(result -> {
				log.info("Message sent successfully to main topic: {}", message.value());
			}).exceptionally(ex -> {
				// Xử lý lỗi nếu gửi thất bại
				log.error("Error sending message to main topic: {}", ex.getMessage(), ex);
				// Ném exception để kích hoạt retry
				throw new RuntimeException("Error sending message to main topic", ex);
			});

		} catch (Exception e) {
			// Log lỗi nếu không thể gửi lại message
			log.error("Error processing message from DLQ: {}", e.getMessage(), e);
			throw e; // Propagate exception to trigger retry
		}
	}

}
