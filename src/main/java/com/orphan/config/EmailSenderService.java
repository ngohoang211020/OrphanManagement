package com.orphan.config;

import com.orphan.common.request.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String initEmail;
    @Autowired
    private TemplateEngine templateEngine;


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

    public void sendEmailWithAttachment(MailTemplate mailTemplate)
            throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);
        String[] stockArr = new String[mailTemplate.getRecipients().size()];
        mimeMessageHelper.setFrom(initEmail);
        mimeMessageHelper.setTo(mailTemplate.getRecipients().toArray(stockArr));
        mimeMessageHelper.setText(mailTemplate.getBody(), true);
        mimeMessageHelper.setSubject(mailTemplate.getSubject());

        mailSender.send(mimeMessage);
        System.out.println("Mail Send...");

    }
}
