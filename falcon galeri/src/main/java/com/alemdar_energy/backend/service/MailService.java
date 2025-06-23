package com.alemdar_energy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void mailSend(String to, String token) {
        String subject = "Şifre Sıfırlama Bağlantınız";
        String resetUrl = "http://localhost:8080/pas/resetPassword?token=" + token;
        String body = "Şifrenizi sıfırlamak için aşağıdaki bağlantıya tıklayın:\n\n" + resetUrl;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);

            System.out.println("Mail başarıyla gönderildi: " + to);
        } catch (MessagingException e) {
            System.err.println("Mail gönderme hatası: " + e.getMessage());
        }
    }
}
