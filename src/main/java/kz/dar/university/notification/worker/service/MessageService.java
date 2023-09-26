package kz.dar.university.notification.worker.service;

import kz.dar.university.notification.worker.domain.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final EmailService emailService;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.test.topic}",
            containerFactory = "singleFactory"
    )
    public void consumeMessages(EmailDTO email) throws MessagingException, IOException {
        log.info("CONSUMED: " + email);
        emailService.sendEmailWithAttachment(email);
    }

}
