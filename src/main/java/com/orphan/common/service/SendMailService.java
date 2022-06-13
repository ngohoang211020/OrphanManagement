package com.orphan.common.service;

import com.orphan.common.entity.MailTrackingEntity;
import com.orphan.common.request.MailTemplate;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@Transactional
public class SendMailService {

    private final Logger logger = LoggerFactory.getLogger(SendMailService.class);

    //    @Autowired
//    private MailTemplateRepository mailTemplateRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String initEmail;
    private Long count = 0L;

    public Long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    //mapper
    public MailTrackingEntity toEntity(MailTemplate mailTemplate) {
        MailTrackingEntity mailTrackingEntity = new MailTrackingEntity();
        mailTrackingEntity.setRecipients(
                mailTemplate.getRecipients().toString().replace("[", "").replace("]", ""));
        mailTrackingEntity.setTitle(mailTemplate.getSubject());
        mailTrackingEntity.setContent(mailTemplate.getBody());

        return mailTrackingEntity;
    }

}
