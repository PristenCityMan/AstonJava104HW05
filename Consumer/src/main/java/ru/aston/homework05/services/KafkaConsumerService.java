package ru.aston.homework05.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.aston.homework05.dto.UserEvent;

@Service
public class KafkaConsumerService {
    private final EmailService emailService;

    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-notifications", groupId = "notification-group")
    public void consume(UserEvent event) {
        emailService.sendNotification(event.getAction(), event.getEmail());
    }
}
