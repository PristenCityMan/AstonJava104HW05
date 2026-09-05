package ru.aston.homework05.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.homework05.dto.UserEvent;
import ru.aston.homework05.services.EmailService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody UserEvent event) {
        emailService.sendNotification(event.getAction(), event.getEmail());
        return ResponseEntity.ok().build();
    }
}
