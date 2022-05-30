package com.orphan.common.service;

import com.orphan.common.entity.FeedbackEntity;
import com.orphan.common.entity.MailTrackingEntity;
import com.orphan.common.repository.FeedBackRepository;
import com.orphan.common.repository.MailTrackingRepository;
import com.orphan.common.request.SendMailDto;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.MailTrackingType;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

//    private final SendMailService sendMailService;
//
//    private final EmailSenderService emailSenderService;

    private final FeedBackRepository feedBackRepository;
    private final MailTrackingRepository mailTrackingRepository;

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

    public void createMailTracking(SendMailDto mailTemplate) throws NotFoundException {
        MailTrackingEntity mailTrackingEntity = toEntity(mailTemplate);
        mailTrackingEntity.setCreatedId(getCurrentUserLogged().getCreatedId());
        mailTrackingRepository.saveAndFlush(mailTrackingEntity);
    }

    public PageInfo<SendMailDto> viewMailNotifyByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
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


    //mapper

    private SendMailDto toDto(MailTrackingEntity mailTrackingEntity) {
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.setId(mailTrackingEntity.getId());
        sendMailDto.setBody(mailTrackingEntity.getContent());
        sendMailDto.setDateSend(OrphanUtils.DateTimeToString(mailTrackingEntity.getDateSend()));
        sendMailDto.setIsAllRole(mailTrackingEntity.getIsAllRole());
        sendMailDto.setRecipients(mailTrackingEntity.getRecipients() != null ? Arrays.stream(mailTrackingEntity.getRecipients().split(",")).collect(Collectors.toList()) : null);
        sendMailDto.setRoles(mailTrackingEntity.getRoles() != null ? Arrays.stream(mailTrackingEntity.getRecipients().split(",")).collect(Collectors.toList()) : null);
        sendMailDto.setIsCompleted(mailTrackingEntity.getIsCompleted());
        sendMailDto.setIsSendImmediately(mailTrackingEntity.getIsSendImmediately());
        sendMailDto.setSubject(mailTrackingEntity.getTitle());
        return sendMailDto;
    }

    private MailTrackingEntity toEntity(SendMailDto mailTemplate) {
        MailTrackingEntity mailTrackingEntity = new MailTrackingEntity();
        mailTrackingEntity.setContent(mailTemplate.getBody());
        mailTrackingEntity.setTitle(mailTemplate.getSubject());
        switch (mailTemplate.getType()) {
            case "MAIL_FEEDBACK":
                mailTrackingEntity.setType(MailTrackingType.MAIL_FEEDBACK.getCode());
                break;
            case "MAIl_TEMPLATE":
                mailTrackingEntity.setType(MailTrackingType.MAIL_TEMPLATE.getCode());
                break;
            case "MAIL_NOTIFY":
                mailTrackingEntity.setType(MailTrackingType.MAIL_NOTIFY.getCode());
                break;
            default:
                mailTrackingEntity.setType(MailTrackingType.MAIL_TEMPLATE.getCode());
                break;
        }

        if (mailTemplate.getIsSendImmediately()) {
            mailTrackingEntity.setIsSendImmediately(true);
            mailTrackingEntity.setDateSend(LocalDateTime.now());
        } else {
            mailTrackingEntity.setDateSend(OrphanUtils.StringToDateTime(mailTemplate.getDateSend()));
        }
        if (mailTemplate.getIsAllRole()) {
            mailTrackingEntity.setIsAllRole(true);
            mailTrackingEntity.setRecipients(null);
            mailTrackingEntity.setRoles(null);
        } else if (!mailTemplate.getRoles().isEmpty()) {
            mailTrackingEntity.setIsAllRole(false);
            mailTrackingEntity.setRoles(mailTemplate.getRoles().toString().replace("[", "").replace("]", ""));
            mailTrackingEntity.setRecipients(null);
        } else if (!mailTemplate.getRecipients().isEmpty()) {
            mailTrackingEntity.setRecipients(mailTemplate.getRecipients().toString().replace("[", "").replace("]", ""));
            mailTrackingEntity.setIsAllRole(false);
            mailTrackingEntity.setRoles(null);
        }

        mailTrackingEntity.setCreatedId(getCurrentUserLogged().getCreatedId());
        Optional<FeedbackEntity> feedbackEntityOptional = feedBackRepository.findById(mailTemplate.getFeedBackId());
        if (feedbackEntityOptional.isPresent()) {
            mailTrackingEntity.setFeedback(feedbackEntityOptional.get());
        } else {
            mailTrackingEntity.setFeedback(null);
        }
        return mailTrackingEntity;
    }
}