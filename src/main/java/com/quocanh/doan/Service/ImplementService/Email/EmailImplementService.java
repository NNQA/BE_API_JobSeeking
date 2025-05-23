package com.quocanh.doan.Service.ImplementService.Email;

import com.nimbusds.jose.util.IOUtils;
import com.quocanh.doan.Service.Interface.IEmailService.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailImplementService implements IEmailService {

    @Value("${spring.mail.username}")
    private String fromMail;
    private final JavaMailSender javaMailSender;

    public EmailImplementService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Override
    public void sendMailRegister(String toEmail, String token) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/SendMailRegisterFile.html");
            InputStream inputStream = resource.getInputStream();
            String htmlContent = IOUtils.readInputStreamToString(inputStream, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("YOUR_TOKEN", token);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(fromMail);
            helper.setTo(toEmail);
            helper.setSubject("Email Verifycation");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMailRetrievePassword(String toEmail, String token) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/SendMailRetrievePassword.html");
            InputStream inputStream = resource.getInputStream();
            String htmlContent = IOUtils.readInputStreamToString(inputStream, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("YOUR_TOKEN", token);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(fromMail);
            helper.setTo(toEmail);
            helper.setSubject("Retrieve your password");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
