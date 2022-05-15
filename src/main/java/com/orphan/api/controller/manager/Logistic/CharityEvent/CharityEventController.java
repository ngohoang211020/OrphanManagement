package com.orphan.api.controller.manager.Logistic.CharityEvent;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.BenefactorListRequest;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.CharityEventDetailDto;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.CharityEventRequest;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.CharityRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.CharityEventService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.PageableConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manager/charity")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC')")
@RequiredArgsConstructor

public class CharityEventController {

    private final CharityEventService charityEventService;

//    @ApiOperation("Get All CharityEvent")
//    @GetMapping("/all")
//    public APIResponse<?> viewAllEvent() throws NotFoundException {
//        return APIResponse.okStatus(charityEventService.viewAllEvent());
//    }

    @ApiOperation("Get CharityEvent By Pages")
    @GetMapping
    public APIResponse<?> viewEventByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<CharityEventDetailDto> eventDtoPageInfo;
        if (page != null) {
            eventDtoPageInfo = charityEventService.viewEventByPage(page, PageableConstants.limit);
        } else {
            eventDtoPageInfo = charityEventService.viewEventByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(eventDtoPageInfo);
    }

    @ApiOperation("View CharityEvent Detail")
    @GetMapping("/{charityEventId}")
    public APIResponse<?> viewEventDetail(@PathVariable("charityEventId") Integer charityEventId) throws NotFoundException {
        CharityEventDetailDto eventDto = charityEventService.viewEventDetail(charityEventId);
        return APIResponse.okStatus(eventDto);
    }

    @ApiOperation("Create new CharityEvent")
    @PostMapping
    public APIResponse<?> createCharityEvent(@Valid @RequestBody CharityRequest charityRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        CharityRequest charityEvent = charityEventService.create(charityRequest);
        return APIResponse.okStatus(charityEvent);
    }

    @ApiOperation("Update CharityEvent detail")
    @PutMapping("/{charityEventId}")
    public APIResponse<?> updateCharityEvent(@PathVariable("charityEventId") Integer charityEventId, @Valid @RequestBody CharityRequest charityEventRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        charityEventRequest.setId(charityEventId);
        CharityRequest charityEvent = charityEventService.update(charityEventRequest);
        return APIResponse.okStatus(charityEvent);
    }

    @ApiOperation("Delete CharityEvent")
    @DeleteMapping("/{charityEventId}")
    public APIResponse<?> deleteCharityEvent(@PathVariable("charityEventId") Integer eventId) throws NotFoundException {
        charityEventService.deleteById(eventId);
        return APIResponse.okStatus();
    }

    @ApiOperation("Add list Benefactor")
    @PostMapping("/benefactors")
    public APIResponse<?> createListBenefactorsForCharityEvent(@RequestBody BenefactorListRequest benefactorListRequest) {
        benefactorListRequest= charityEventService.createListBenefactorForCharity(benefactorListRequest);
        return APIResponse.okStatus(benefactorListRequest);
    }
}
