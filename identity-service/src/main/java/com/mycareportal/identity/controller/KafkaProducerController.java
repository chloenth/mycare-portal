package com.mycareportal.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycareportal.event.dto.KafkaMessage;
import com.mycareportal.identity.dto.request.userprofile.UserProfileRequest;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final String TOPIC = "user-profile-created-topic";

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody KafkaMessage<UserProfileRequest> message) {
        try {
            kafkaTemplate.send(TOPIC, message);
            return ResponseEntity.ok("Message sent to Kafka");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }
}
