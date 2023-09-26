package kz.dar.university.notification.worker.service;

import kz.dar.university.notification.worker.domain.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sendEmail;

    public void sendSimpleMail(EmailDTO email) {

        log.info("EMAIL TO SEND: " + email);
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sendEmail);
            mailMessage.setTo(email.getRecipient());
            mailMessage.setText(email.getContent());
            mailMessage.setSubject(email.getTitle());

            log.info("EMAIL: " + mailMessage);
            javaMailSender.send(mailMessage);
            log.info("Mail Sent Successfully...");
        } catch (Exception e) {
            log.error("Error while Sending Mail: " + e);
        }
    }

    public void sendHtmlEmail(EmailDTO email) throws MessagingException {
        MimeMessage message = getMimeMessage(email);

        String htmlContent = "<h1>" + email.getTitle() + "</h1>" +
                "<p><strong>" + email.getContent() + "</strong></p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }

    public void sendEmailFromTemplate(EmailDTO email) throws MessagingException, IOException {
        MimeMessage message = getMimeMessage(email);

        String htmlTemplate = "";
        InputStream resource = new ClassPathResource(
                "notification.html").getInputStream();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource))) {
            htmlTemplate = reader.lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            log.error("Error while buffered reader: " + ex);
        }

        // Replace placeholders in the HTML template with dynamic values
        htmlTemplate = htmlTemplate.replace(
                "${title}",
                String.format("Hi, %s %s", email.getRecipient(), email.getTitle())
        );
        htmlTemplate = htmlTemplate.replace(
                "${content}",
                email.getContent()
        );

        // Set the email's content to be the HTML template
        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }

    private MimeMessage getMimeMessage(EmailDTO email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress(sendEmail));
        message.setRecipients(MimeMessage.RecipientType.TO, email.getRecipient());
        message.setSubject(email.getTitle());

        return message;
    }

    public void sendEmailWithAttachment(EmailDTO email) throws MessagingException, IOException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email.getRecipient());
        helper.setSubject(email.getTitle());
        helper.setText(email.getContent());

        ClassPathResource classPathResource = new ClassPathResource("notification.html");
        File file = classPathResource.getFile();
        helper.addAttachment("notification.html", file);

        javaMailSender.send(message);
    }

}
