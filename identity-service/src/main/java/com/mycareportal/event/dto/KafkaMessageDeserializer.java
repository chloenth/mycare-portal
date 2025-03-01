package com.mycareportal.event.dto;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaMessageDeserializer<T> implements Deserializer<KafkaMessage<T>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KafkaMessage<T> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, KafkaMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing KafkaMessage", e);
        }
    }
}
