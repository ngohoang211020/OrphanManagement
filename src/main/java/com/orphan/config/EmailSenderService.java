package com.orphan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String initEmail;

	public void sendEmailWithAttachment(String toEmail, String body, String subject)
            throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(initEmail);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body, true);
        mimeMessageHelper.setSubject(subject);

        mailSender.send(mimeMessage);
        System.out.println("Mail Send...");

    }
}
