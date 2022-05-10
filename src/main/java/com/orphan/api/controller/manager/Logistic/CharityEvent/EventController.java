package com.orphan.api.controller.manager.Logistic.CharityEvent;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.EventRequest;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.EventDto;
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

public class EventController {

    private final CharityEventService charityEventService;

    @ApiOperation("Get All CharityEvent")
    @GetMapping("/all")
    public APIResponse<?> viewAllEvent() throws NotFoundException {
        return APIResponse.okStatus(charityEventService.viewAllEvent());
    }

    @ApiOperation("Get CharityEvent By Pages")
    @GetMapping
    public APIResponse<?> viewEventByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<EventDto> eventDtoPageInfo;
        if (page != null) {
            eventDtoPageInfo = charityEventService.viewEventByPage(page, PageableConstants.limit);
        } else {
            eventDtoPageInfo = charityEventService.viewEventByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(eventDtoPageInfo);
    }

    @ApiOperation("View CharityEvent Detail")
    @GetMapping("/{eventId}")
    public APIResponse<?> viewEventDetail(@PathVariable("eventId") Integer eventId) throws NotFoundException {
        EventDto eventDto = charityEventService.viewEventDetail(eventId);
        return APIResponse.okStatus(eventDto);
    }

    @ApiOperation("Create new CharityEvent")
    @PostMapping
    public APIResponse<?> createCharityEvent(@Valid @RequestBody EventRequest eventRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        eventRequest = charityEventService.createCharityEvent(eventRequest);
        return APIResponse.okStatus(eventRequest);
    }

    @ApiOperation("Update CharityEvent detail")
    @PutMapping("/{eventId}")
    public APIResponse<?> updateCharityEvent(@PathVariable("eventId") Integer eventId, @Valid @RequestBody EventRequest eventRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        eventRequest = charityEventService.updateCharityEvent(eventRequest, eventId);
        return APIResponse.okStatus(eventRequest);
    }

    @ApiOperation("Delete CharityEvent")
    @DeleteMapping("/{eventId}")
    public APIResponse<?> deleteCharityEvent(@PathVariable("eventId") Integer eventId) throws NotFoundException {
        charityEventService.deleteById(eventId);
        return APIResponse.okStatus();
    }

}
