package com.orphan.api.controller.home;

import com.orphan.api.controller.manager.HR.FeedBack.dto.FeedbackDto;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.CharityEventDetailDto;
import com.orphan.api.controller.manager.Logistic.Picnic.dto.PicnicDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.CharityEventService;
import com.orphan.common.service.FeedbackService;
import com.orphan.common.service.PicnicService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
    private final CharityEventService charityEventService;
    private final PicnicService picnicService;

    private final FeedbackService feedbackService;
    @ApiOperation("Get CharityEvent By Pages")
    @GetMapping("/charity")
    public APIResponse<?> viewEventByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            , @ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<CharityEventDetailDto> eventDtoPageInfo;
        if (page != null) {
            eventDtoPageInfo = charityEventService.viewEventByPage(page, limit);
        } else {
            eventDtoPageInfo = charityEventService.viewEventByPage(1, limit);

        }
        return APIResponse.okStatus(eventDtoPageInfo);
    }

    @ApiOperation("View CharityEvent Detail")
    @GetMapping("/charity/{charityEventId}")
    public APIResponse<?> viewEventDetail(@PathVariable("charityEventId") Integer charityEventId) throws NotFoundException {
        CharityEventDetailDto eventDto = charityEventService.viewEventDetail(charityEventId);
        return APIResponse.okStatus(eventDto);
    }

    @ApiOperation("Get Picnics By Pages")
    @GetMapping("/picnic")
    public APIResponse<?> viewPicnicByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<PicnicDto> eventDtoPageInfo;
        if (page != null) {
            eventDtoPageInfo = picnicService.viewPicnicsByPage(page, limit);
        } else {
            eventDtoPageInfo = picnicService.viewPicnicsByPage(1, limit);

        }
        return APIResponse.okStatus(eventDtoPageInfo);
    }

    @ApiOperation("View Picnic Detail")
    @GetMapping("/picnic/{picnicId}")
    public APIResponse<?> viewPicnicDetail(@PathVariable("picnicId") Integer picnicId) throws NotFoundException {
        return APIResponse.okStatus(picnicService.viewDetailPicnic(picnicId));
    }

    @ApiOperation("Create Feedback")
    @PostMapping("/feedback")
    public APIResponse<?> createFeedback(@Valid @RequestBody FeedbackDto feedbackDto) throws NotFoundException {
        return APIResponse.okStatus(feedbackService.create(feedbackDto));
    }
}
