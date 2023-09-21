package kz.dar.university.notification.worker.service;

import kz.dar.university.notification.worker.domain.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendSimpleMail(EmailDTO email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom("no-reply@mail.ru");
            mailMessage.setTo(email.getRecipient());
            mailMessage.setText(email.getContent());
            mailMessage.setSubject(email.getTitle());

            javaMailSender.send(mailMessage);
            log.info("Mail Sent Successfully...");
        } catch (Exception e) {
            log.error("Error while Sending Mail: " + e);
        }
    }

}
