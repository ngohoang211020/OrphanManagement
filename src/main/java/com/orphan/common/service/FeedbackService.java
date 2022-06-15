package com.orphan.common.service;


import com.orphan.api.controller.manager.HR.FeedBack.dto.FeedbackDetail;
import com.orphan.api.controller.manager.HR.FeedBack.dto.FeedbackDto;
import com.orphan.common.entity.FeedbackEntity;
import com.orphan.common.entity.MailTrackingEntity;
import com.orphan.common.repository.FeedBackRepository;
import com.orphan.common.repository.MailTrackingRepository;
import com.orphan.common.request.MailTemplate;
import com.orphan.common.vo.PageInfo;
import com.orphan.config.EmailSenderService;
import com.orphan.enums.MailTrackingType;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import java.time.LocalDateTime;
import java.util.List;
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
public class FeedbackService extends BaseService {
    private final FeedBackRepository feedBackRepository;
    private final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    private final MailTrackingRepository mailTrackingRepository;

    private final SendMailService sendMailService;
    private final EmailSenderService emailSenderService;


    public FeedbackEntity findById(Integer feedBackId) throws NotFoundException {
        Optional<FeedbackEntity> feedbackEntityOptional = feedBackRepository.findById(feedBackId);
        if (!feedbackEntityOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FEEDBACK_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FEEDBACK));
        }
        return feedbackEntityOptional.get();
    }

    public FeedbackDetail create(FeedbackDto feedbackDto) {
        FeedbackEntity feedbackEntity = toEntity(feedbackDto);
        feedbackEntity.setCreatedId(String.valueOf(getCurrentUserId()));
        feedbackEntity.setIsReplied(false);
        this.feedBackRepository.save(feedbackEntity);
        feedbackDto.setId(feedbackEntity.getId());
        return toDto(feedbackEntity);
    }

    public void replyFeedBack(MailTemplate mailTemplate, Integer feedBackId) throws NotFoundException, MessagingException {

        MailTrackingEntity mailTrackingEntity = sendMailService.toEntity(mailTemplate);

        try {
//            emailSenderService.sendEmailWithAttachment(mailTemplate);
            FeedbackEntity feedbackEntity = findById(feedBackId);
            feedbackEntity.setIsReplied(true);
            feedbackEntity.setDateReply(LocalDateTime.now());
            mailTrackingEntity.setDateSend(LocalDateTime.now());
            mailTrackingEntity.setIsCompleted(false);
            mailTrackingEntity.setIsSendImmediately(true);
            mailTrackingEntity.setFeedback(feedbackEntity);
            mailTrackingEntity.setCreatedId(getCurrentUserId().toString());
            mailTrackingEntity.setType(MailTrackingType.MAIL_FEEDBACK.getCode());
        } catch (Exception ex) {
            log.info("sendEmail error, error msg: {}", ex.getMessage());
//            mailTrackingEntity.setIsCompleted(false);
        }
        mailTrackingRepository.save(mailTrackingEntity);
    }

    public PageInfo<FeedbackDetail> viewFeedbacksByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("createdAt").descending());
        Page<FeedbackEntity> feedbackEntityPage = feedBackRepository.findByOrderByCreatedAtDesc(
                pageRequest);
        if (feedbackEntityPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FEEDBACK_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FEEDBACK));
        }
        List<FeedbackDetail> feedbackDetailList = feedbackEntityPage.getContent().stream().map(this::toDto).collect(Collectors.toList());
        PageInfo<FeedbackDetail> feedbackDetailPageInfo = new PageInfo<>();
        feedbackDetailPageInfo.setPage(page);
        feedbackDetailPageInfo.setLimit(limit);
        feedbackDetailPageInfo.setResult(feedbackDetailList);
        feedbackDetailPageInfo.setTotal(feedbackEntityPage.getTotalElements());
        feedbackDetailPageInfo.setPages(feedbackEntityPage.getTotalPages());
        return feedbackDetailPageInfo;
    }
    //mapper
    private FeedbackEntity toEntity(FeedbackDto feedbackDto) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setContent(feedbackDto.getBody());
        feedbackEntity.setFullName(feedbackDto.getFullName());
        feedbackEntity.setEmail(feedbackDto.getEmail());
        feedbackEntity.setTitle(feedbackDto.getSubject());
        return feedbackEntity;
    }

    private FeedbackDetail toDto(FeedbackEntity feedbackEntity) {
      FeedbackDetail feedbackDetail = new FeedbackDetail();
        feedbackDetail.setId(feedbackEntity.getId());
        feedbackDetail.setBody(feedbackEntity.getContent());
        feedbackDetail.setFullName(feedbackEntity.getFullName());
        feedbackDetail.setDateReply(OrphanUtils.DateTimeToString(feedbackEntity.getDateReply()));
        feedbackDetail.setEmail(feedbackEntity.getEmail());
        feedbackDetail.setSubject(feedbackEntity.getTitle());
        feedbackDetail.setIsReplied(feedbackEntity.getIsReplied());
        feedbackDetail.setDateFeedback(OrphanUtils.DateTimeToString(feedbackEntity.getCreatedAt()));
        return feedbackDetail;
    }
}
