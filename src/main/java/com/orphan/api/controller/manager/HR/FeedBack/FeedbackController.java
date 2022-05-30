package com.orphan.api.controller.manager.HR.FeedBack;

import com.orphan.api.controller.manager.HR.FeedBack.dto.FeedbackDetail;
import com.orphan.common.request.MailTemplate;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.FeedbackService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/v1/manager/feedback")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_HR')")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @ApiOperation("Get Feedbacks By Pages")
    @GetMapping
    public APIResponse<?> viewFeedbacksByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            , @ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<FeedbackDetail> feedbackDetailPageInfo;
        if (page != null) {
            feedbackDetailPageInfo = feedbackService.viewFeedbacksByPage(page, limit);
        } else {
            feedbackDetailPageInfo = feedbackService.viewFeedbacksByPage(1, limit);

        }
        return APIResponse.okStatus(feedbackDetailPageInfo);
    }

    @ApiOperation("Reply Feedback")
    @PostMapping("/{feedBackId}")
    public APIResponse<?> replyFeedback(@RequestBody MailTemplate mailTemplate, @PathVariable("feedBackId") Integer feedBackId) throws NotFoundException, MessagingException {
        feedbackService.replyFeedBack(mailTemplate, feedBackId);
        return APIResponse.okStatus();
    }
}
