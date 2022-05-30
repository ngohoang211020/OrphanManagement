package com.orphan.common.service;

import com.orphan.api.controller.manager.HR.FeedBack.dto.FeedbackDetail;
import com.orphan.common.entity.FeedbackEntity;
import com.orphan.common.entity.MailTrackingEntity;
import com.orphan.common.repository.MailTrackingRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MailTrackingService extends BaseService{
    private final MailTrackingRepository mailTrackingRepository;

//    public PageInfo<FeedbackDetail> viewMailTrackingByPage(Integer page, Integer limit) throws NotFoundException {
//        PageRequest pageRequest = buildPageRequest(page, limit);
//        Page<MailTrackingEntity> feedbackEntityPage = mailTrackingRepository.findByOrderByDateSendDesc(pageRequest);
//        if (feedbackEntityPage.getContent().isEmpty()) {
//            throw new NotFoundException(NotFoundException.ERROR_FEEDBACK_NOT_FOUND,
//                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FEEDBACK));
//        }
//        List<FeedbackDetail> feedbackDetailList = feedbackEntityPage.getContent().stream().map(this::toDto).collect(Collectors.toList());
//        PageInfo<FeedbackDetail> feedbackDetailPageInfo = new PageInfo<>();
//        feedbackDetailPageInfo.setPage(page);
//        feedbackDetailPageInfo.setLimit(limit);
//        feedbackDetailPageInfo.setResult(feedbackDetailList);
//        feedbackDetailPageInfo.setTotal(feedbackEntityPage.getTotalElements());
//        feedbackDetailPageInfo.setPages(feedbackEntityPage.getTotalPages());
//        return feedbackDetailPageInfo;
//    }


}
