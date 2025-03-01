package com.trustrace.ploughing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentLink(String to, String paymentLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Ploughing Service Bill Payment Link");
        message.setText("Click the link to complete your payment: " + paymentLink);
        mailSender.send(message);
    }
}
