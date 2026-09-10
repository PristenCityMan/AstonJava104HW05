package ru.aston.homework05.services;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import ru.aston.homework05.dto.UserEvent;

@Service
public class KafkaConsumerService {
    private final EmailService emailService;

    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2),
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "user-notifications", groupId = "notification-group")
    public void consume(UserEvent event) {
        emailService.sendNotification(event.getAction(), event.getEmail());
    }

    @DltHandler
    public void handleDlt(UserEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Сообщение окончательно не удалось обработать. Отправлено в DLT.");

    }
}
