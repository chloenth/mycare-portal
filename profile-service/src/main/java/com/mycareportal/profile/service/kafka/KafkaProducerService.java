package com.mycareportal.profile.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.mycareportal.event.dto.KafkaMessage;
import com.mycareportal.profile.dto.request.KafkaProfileUpdateRequest;

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
    @Value("${spring.kafka.topic.profile-updated}")
    String profileUpdatedTopic;

    // Inject topic name from application.yml

    public void sendProfileUpdatedEvent(KafkaMessage<KafkaProfileUpdateRequest> event) {
        try {
            log.info("hello from producer");
            log.info("event payload in producer service: {}", event.getPayload());
            kafkaTemplate.send(profileUpdatedTopic, event).get();
            log.info("Message sent sucessfully");

        } catch (Exception e) {
            log.error("Failed to send message, saving to DLQ", e);
            throw new RuntimeException(e);
        }
    }
}
