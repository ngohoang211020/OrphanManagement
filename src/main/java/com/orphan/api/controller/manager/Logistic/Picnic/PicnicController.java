package com.orphan.api.controller.manager.Logistic.Picnic;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Logistic.Picnic.dto.PicnicDto;
import com.orphan.api.controller.manager.Logistic.Picnic.dto.PicnicRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.PicnicService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager/entertainment")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC')")
@RequiredArgsConstructor
public class PicnicController {

    private final PicnicService picnicService;

    @ApiOperation("Get All Entertainments")
    @GetMapping("/all")
    public APIResponse<?> viewAll() throws NotFoundException {
        return APIResponse.okStatus(picnicService.viewAllPicnic());
    }

    @ApiOperation("Get Entertainments By Pages")
    @GetMapping
    public APIResponse<?> viewEventByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<PicnicDto> eventDtoPageInfo;
        if (page != null) {
            eventDtoPageInfo = picnicService.viewPicnicsByPage(page, limit);
        } else {
            eventDtoPageInfo = picnicService.viewPicnicsByPage(1, limit);

        }
        return APIResponse.okStatus(eventDtoPageInfo);
    }

    @ApiOperation("View Entertainments Detail")
    @GetMapping("/{picnicId}")
    public APIResponse<?> viewEventDetail(@PathVariable("picnicId") Integer picnicId) throws NotFoundException {
        return APIResponse.okStatus(picnicService.viewDetailPicnic(picnicId));
    }

    @ApiOperation("Create new Entertainment")
    @PostMapping
    public APIResponse<?> createEntertainment(@Valid @RequestBody PicnicRequest picnicRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        picnicRequest = picnicService.createPicnic(picnicRequest);
        return APIResponse.okStatus(picnicRequest);
    }

    @ApiOperation("Update Entertainment detail")
    @PutMapping("/{enterId}")
    public APIResponse<?> updateEntertainments(@PathVariable("enterId") Integer enterId, @Valid @RequestBody PicnicRequest picnicRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        picnicRequest = picnicService.updatePicnic(picnicRequest, enterId);
        return APIResponse.okStatus(picnicRequest);
    }

    @ApiOperation("Delete Entertainment")
    @DeleteMapping("/{enterId}")
    public APIResponse<?> deleteEntertainments(@PathVariable("enterId") Integer enterId) throws NotFoundException {
        picnicService.deleteById(enterId);
        return APIResponse.okStatus();
    }

}
