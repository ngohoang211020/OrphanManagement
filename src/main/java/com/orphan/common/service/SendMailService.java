package com.orphan.common.service;

import com.orphan.common.entity.MailTrackingEntity;
import com.orphan.common.request.MailTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.transaction.Transactional;

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

//    @Value("${mail.retryable}")
//    private Integer retryable;

    private Long count = 0L;

    public Long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }

//    public void sendMail(MailDTO mailDTO, Boolean isTemplate) throws MessagingException {
//        logger.info("Sending mail...");
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//            MimeMessageHelper mimeMessageHelper
//                    = new MimeMessageHelper(mimeMessage, true, "UTF8");
//            if (mailDTO.getRecipients() != null) {
//                mimeMessageHelper.setTo(mailDTO.getRecipients().toArray(new String[0]));
//            }
//            if (mailDTO.getCc() != null) {
//                mimeMessageHelper.setCc(mailDTO.getCc().toArray(new String[0]));
//            }
//            if (mailDTO.getBcc() != null) {
//                mimeMessageHelper.setBcc(mailDTO.getBcc().toArray(new String[0]));
//            }
//            mimeMessageHelper.setFrom(Objects.requireNonNull(initEmail));
//            if (Boolean.TRUE.equals(isTemplate) && mailDTO.getTemplate() != null) {
//                MailTemplateEntity mailTemplate = mailTemplateRepository.findByName(mailDTO.getTemplate().getName());
//                if (mailTemplate == null) {
//                    logger.error("Template {} not found", mailDTO.getTemplate().getName());
//                    return;
//                }
//
//                Context context = new Context();
//                context.setVariables(mailDTO.getTemplate().getVariables());
//
//                String subject = templateEngine.process(mailTemplate.getSubject(), context);
//                subject = SubjectUtils.removeTagHtml(subject);
//
//                String html = MailTemplateConstants.HTML;
//                html = html.replace(MailTemplateConstants.TEMPLATE, mailTemplate.getBody());
//                String body = templateEngine.process(html, context);
//                mimeMessageHelper.setSubject(subject);
//
//                mimeMessageHelper.setText(body, true);
//
//                mailSender.send(mimeMessage);
//                logger.info("...Saved mail tracking");
//            } else {
//                mimeMessageHelper.setText(mailDTO.getNoTemplate().getBody());
//                mimeMessageHelper.setSubject(mailDTO.getNoTemplate().getSubject());
//                mailSender.send(mimeMessage);
//                logger.info("...Sent mail");
//            }
//
//            logger.info("...Sent mail to {} and cc {} and bcc {}",
//                    (mimeMessageHelper.getMimeMessage().getRecipients(MimeMessage.RecipientType.TO)));
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }


//    private void setRetryable(MailDTO dto) {
//        dto.getRecipients().removeAll(Collections.singletonList(StringUtils.EMPTY));
//        dto.getCc().removeAll(Collections.singletonList(StringUtils.EMPTY));
//        dto.getBcc().removeAll(Collections.singletonList(StringUtils.EMPTY));
//        boolean isRecipient = CollectionUtils.isEmpty(dto.getRecipients()) &&  CollectionUtils.isEmpty(
//                dto.getCc()) &&  CollectionUtils.isEmpty(dto.getBcc());
//        if(isRecipient){
//            this.setCount(retryable);
//        }
//    }
//
//    public void initialCountIndex() {
//        this.count = 0L;
//        logger.info("Count index start to 0");
//    }

    //mapper
    public MailTrackingEntity toEntity(MailTemplate mailTemplate){
        MailTrackingEntity mailTrackingEntity=new MailTrackingEntity();
        mailTrackingEntity.setRecipients(mailTemplate.getRecipients().toString());
        mailTrackingEntity.setTitle(mailTemplate.getSubject());
        mailTrackingEntity.setContent(mailTemplate.getBody());

        return mailTrackingEntity;
    }

}
