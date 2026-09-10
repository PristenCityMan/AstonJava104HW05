package ru.aston.homework05;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import ru.aston.homework05.dto.UserEvent;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" }
)
public class NotificationIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDirectEmailNotificationApi() throws Exception {

        UserEvent event = new UserEvent("CREATE", "test@example.com");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/notifications/send", event, Void.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages.length).isEqualTo(1);

        MimeMessage message = receivedMessages[0];
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("test@example.com");
        assertThat(message.getContent().toString()).contains("Ваш аккаунт на сайте ваш сайт был успешно создан.");
    }
}
