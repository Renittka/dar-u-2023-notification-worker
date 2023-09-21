package kz.dar.university.notification.worker.service;

import kz.dar.university.notification.worker.domain.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final EmailService emailService;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.test.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMessages(EmailDTO email) {
        log.info("CONSUMED: " + email);
        emailService.sendSimpleMail(email);
    }

}
