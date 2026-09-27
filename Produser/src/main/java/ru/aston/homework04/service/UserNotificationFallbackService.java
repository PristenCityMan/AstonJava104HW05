package ru.aston.homework04.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.aston.homework04.dto.UserDto;
import ru.aston.homework04.dto.UserEvent;

@Service
public class UserNotificationFallbackService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String NOTIFICATION_REST_URL = "http://NOTIFICATION-SERVICE/api/notifications/send";

    @CircuitBreaker(name = "notificationServiceCB", fallbackMethod = "fallbackNotification")
    public void sendNotificationViaRest(String action, String email) {
        UserEvent event = new UserEvent(action, email);

        restTemplate.postForEntity(NOTIFICATION_REST_URL, event, Void.class);
    }


    public void fallbackNotification(String action, String email, Throwable throwable) {
        System.err.printf("Circuit Breaker сработал! Сервис уведомлений недоступен. Ошибка: %s. " +
                "Событие [%s] для %s сохранено в локальный лог.%n", throwable.getMessage(), action, email);
    }

}
