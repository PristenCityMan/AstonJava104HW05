package ru.aston.homework05.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(String action, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);

        if ("CREATE".equalsIgnoreCase(action)) {
            message.setSubject("Регистрация аккаунта");
            message.setText("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
        } else if ("DELETE".equalsIgnoreCase(action)) {
            message.setSubject("Удаление аккаунта");
            message.setText("Здравствуйте! Ваш аккаунт был удалён.");
        } else {
            return;
        }

        mailSender.send(message);
    }
}
