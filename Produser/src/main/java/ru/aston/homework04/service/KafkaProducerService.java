package ru.aston.homework04.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.homework04.dto.UserEvent;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.notification-topic}")
    private String topic;

    public KafkaProducerService(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(String action, String email) {
        kafkaTemplate.send(topic, new UserEvent(action, email));
    }
}