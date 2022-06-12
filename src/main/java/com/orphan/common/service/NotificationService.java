package com.orphan.common.service;

import com.orphan.common.entity.MailTrackingEntity;
import com.orphan.common.entity.User;
import com.orphan.common.entity.UserNotifyEntity;
import com.orphan.common.repository.FeedBackRepository;
import com.orphan.common.repository.MailTrackingRepository;
import com.orphan.common.repository.UserNotifyRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.request.MailTemplate;
import com.orphan.common.request.SendMailDto;
import com.orphan.common.vo.PageInfo;
import com.orphan.config.EmailSenderService;
import com.orphan.enums.MailTrackingType;
import com.orphan.enums.UserStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final EmailSenderService emailSenderService;

    private final FeedBackRepository feedBackRepository;

    private final UserNotifyRepository userNotifyRepository;

    private final MailTrackingRepository mailTrackingRepository;
    private final UserRepository userRepository;

    public MailTrackingEntity findById(Integer id) {
        Optional<MailTrackingEntity> optionalMailTracking = mailTrackingRepository.findById(id);
        if (!optionalMailTracking.isPresent()) {
            new NotFoundException(NotFoundException.ERROR_MAIL_NOTIFY_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.MAIl_NOTIFICATION));
        }
        return optionalMailTracking.get();
    }

    public SendMailDto viewMailTrackingDetail(Integer id) throws NotFoundException {
        MailTrackingEntity mailTrackingEntity = findById(id);
        return toDto(mailTrackingEntity);
    }

    public void createMailTracking(SendMailDto mailTemplate) {
        MailTrackingEntity mailTrackingEntity = toEntity(mailTemplate);
        mailTrackingRepository.save(mailTrackingEntity);
    }

    public PageInfo<SendMailDto> viewMailNotifyByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("dateSend").descending());
        Page<MailTrackingEntity> sendMailDtoPage = mailTrackingRepository.findByOrderByDateSendDesc(pageRequest);
        if (sendMailDtoPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_MAIL_NOTIFY_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.MAIl_NOTIFICATION));
        }
        List<SendMailDto> furnitureDtoList = sendMailDtoPage.getContent().stream().map(sendMailDto -> toDto(sendMailDto)).collect(Collectors.toList());
        PageInfo<SendMailDto> sendMailDtoPageInfo = new PageInfo<>();
        sendMailDtoPageInfo.setPage(page);
        sendMailDtoPageInfo.setLimit(limit);
        sendMailDtoPageInfo.setResult(furnitureDtoList);
        sendMailDtoPageInfo.setTotal(sendMailDtoPage.getTotalElements());
        sendMailDtoPageInfo.setPages(sendMailDtoPage.getTotalPages());
        return sendMailDtoPageInfo;
    }

    public void sendMailImmediately() throws MessagingException {
        List<MailTrackingEntity> mailTrackingEntities = mailTrackingRepository.findByIsSendImmediately(true);
        List<SendMailDto> sendMailDtoList = mailTrackingEntities.stream().map(mailTrackingEntity -> toDto(mailTrackingEntity)).collect(Collectors.toList());
        for (SendMailDto sendMailDto : sendMailDtoList) {
            sendMail(sendMailDto);
        }
    }

    public void sendMailAtDateSend() throws MessagingException {
        List<MailTrackingEntity> mailTrackingEntities = mailTrackingRepository.findByDateSend(LocalDateTime.now());
        List<SendMailDto> sendMailDtos = mailTrackingEntities.stream().map(mailTrackingEntity -> toDto(mailTrackingEntity)).collect(Collectors.toList());
        for (SendMailDto sendMailDto : sendMailDtos) {
            sendMail(sendMailDto);
        }
    }

    public void sendMail(SendMailDto sendMailDto) throws MessagingException {
        MailTemplate mailTemplate = new MailTemplate();
        if (sendMailDto.getIsAllRole()) {
            List<User> users = userRepository.findByUserActived(UserStatus.ACTIVED.getCode());
            mailTemplate.setRecipients(users.stream().map(user -> user.getEmail()).collect(Collectors.toList()));
        } else if (sendMailDto.getRoles()!=null) {
            List<User> users = userRepository.findByRoles_NameIn(sendMailDto.getRoles());
            mailTemplate.setRecipients(users.stream().map(user -> user.getEmail()).collect(Collectors.toList()));
        } else {
            mailTemplate.setRecipients(sendMailDto.getRecipients());
        }
        mailTemplate.setBody(sendMailDto.getBody());
        mailTemplate.setSubject(sendMailDto.getSubject());
        emailSenderService.sendEmailWithAttachment(mailTemplate);
        sendMailDto.setIsCompleted(true);
        saveListUserNotify(mailTemplate, sendMailDto);
        mailTrackingRepository.save(toEntity(sendMailDto));
    }

    //mapper

    public void saveListUserNotify(MailTemplate mailTemplate, SendMailDto sendMailDto) {
        List<UserNotifyEntity> userNotifyEntities = new ArrayList<>();
        User createdUser = userRepository.findById(sendMailDto.getCreatedId()).get();
        for (String mail : mailTemplate.getRecipients()) {
            User user = userRepository.findByEmail(mail).orElse(null);
            UserNotifyEntity userNotifyEntity = new UserNotifyEntity();
            userNotifyEntity.setSender(createdUser);
            userNotifyEntity.setUser(user);
            userNotifyEntity.setContent(sendMailDto.getBody());
            userNotifyEntity.setSubject(sendMailDto.getSubject());
            userNotifyEntity.setDateSend(OrphanUtils.StringToDateTime(sendMailDto.getDateSend()));
            userNotifyEntities.add(userNotifyEntity);
        }
        userNotifyRepository.saveAll(userNotifyEntities);

    }

    private SendMailDto toDto(MailTrackingEntity mailTrackingEntity) {
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.setId(mailTrackingEntity.getId());
        sendMailDto.setBody(Objects.requireNonNull(mailTrackingEntity.getContent(), ""));
        sendMailDto.setDateSend(OrphanUtils.DateTimeToString(mailTrackingEntity.getDateSend()));
        sendMailDto.setIsAllRole(mailTrackingEntity.getIsAllRole());
        if (mailTrackingEntity.getRecipients() != "" && mailTrackingEntity.getRecipients() != null) {
            if (mailTrackingEntity.getRecipients().contains(",")) {
                String[] recipients = mailTrackingEntity.getRecipients().split(",");
                sendMailDto.setRecipients(Arrays.asList(recipients));
            } else {
                sendMailDto.setRecipients(Collections.singletonList(mailTrackingEntity.getRecipients()));
            }
        }
        if (mailTrackingEntity.getRoles() != "" && mailTrackingEntity.getRoles() != null) {

            if (mailTrackingEntity.getRoles().contains(",")) {
                String[] roles = mailTrackingEntity.getRoles().split(",");
                sendMailDto.setRoles(Arrays.asList(roles));
            } else {
                sendMailDto.setRoles(Collections.singletonList(mailTrackingEntity.getRoles()));
            }
        }
        sendMailDto.setType(mailTrackingEntity.getType());
        sendMailDto.setIsCompleted(mailTrackingEntity.getIsCompleted());
        sendMailDto.setIsSendImmediately(mailTrackingEntity.getIsSendImmediately());
        sendMailDto.setSubject(mailTrackingEntity.getTitle());
        sendMailDto.setCreatedId(Integer.parseInt(mailTrackingEntity.getCreatedId()));
        return sendMailDto;
    }

    private MailTrackingEntity toEntity(SendMailDto mailTemplate) {
        MailTrackingEntity mailTrackingEntity = new MailTrackingEntity();
        mailTrackingEntity.setContent(mailTemplate.getBody());
        mailTrackingEntity.setTitle(mailTemplate.getSubject());
        mailTrackingEntity.setIsCompleted(mailTemplate.getIsCompleted());
        mailTrackingEntity.setCreatedId(String.valueOf(mailTemplate.getCreatedId()));
        if (mailTemplate.getId() > 0) {
            mailTrackingEntity.setId(mailTemplate.getId());
        }
        switch (mailTemplate.getType()) {
            case "MAIL_FEEDBACK":
                mailTrackingEntity.setType(MailTrackingType.MAIL_FEEDBACK.getCode());
                break;
            case "MAIL_NOTIFY":
                mailTrackingEntity.setType(MailTrackingType.MAIL_NOTIFY.getCode());
                break;
            default:
                mailTrackingEntity.setType(MailTrackingType.MAIL_TEMPLATE.getCode());
                break;
        }

        if (!mailTemplate.getIsSendImmediately()) {
            mailTrackingEntity.setDateSend(
                    OrphanUtils.StringToDateTime(mailTemplate.getDateSend()));
            mailTrackingEntity.setIsSendImmediately(false);
        } else {
            mailTrackingEntity.setIsSendImmediately(true);
            mailTrackingEntity.setDateSend(LocalDateTime.now());
        }
        if (mailTemplate.getIsAllRole()) {
            mailTrackingEntity.setIsAllRole(true);
            mailTrackingEntity.setRecipients(null);
            mailTrackingEntity.setRoles(null);
        } else if (mailTemplate.getRoles() != null) {
            mailTrackingEntity.setIsAllRole(false);
            mailTrackingEntity.setRoles(
                    mailTemplate.getRoles().toString().replace("[", "").replace("]", ""));
            mailTrackingEntity.setRecipients(null);
        } else if (!mailTemplate.getRecipients().isEmpty()) {
            mailTrackingEntity.setRecipients(
                    mailTemplate.getRecipients().toString().replace("[", "").replace("]", ""));
            mailTrackingEntity.setIsAllRole(false);
            mailTrackingEntity.setRoles(null);
        }
//        Optional<FeedbackEntity> feedbackEntityOptional = null;
//        if(mailTemplate.getFeedBackId().equals(null)){
//            feedbackEntityOptional = feedBackRepository.findById(mailTemplate.getFeedBackId());
//        }
//
//        if (feedbackEntityOptional.isPresent()) {
//            mailTrackingEntity.setFeedback(feedbackEntityOptional.get());
//        } else {
//            mailTrackingEntity.setFeedback(null);
//        }
        return mailTrackingEntity;
    }


}