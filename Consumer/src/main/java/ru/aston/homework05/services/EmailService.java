package ru.aston.homework05.services;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.aston.homework05.models.NotificationType;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(String action, String email) {

        NotificationType notificationType = NotificationType.fromString(action);

        if (notificationType == null) {
            System.out.printf("Неизвестный тип операции для уведомления: %s%n", action);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(notificationType.getSubject());
        message.setText(notificationType.getText());

        try
        {mailSender.send(message);
            System.out.printf("Сообщение на адрес %s успешно отправлено%n", email);
        }
        catch (MailException e)
        {
            System.out.printf("Ошибка при отправке email на адрес %s:%s%n", email, e.getMessage());
        }
    }
}
