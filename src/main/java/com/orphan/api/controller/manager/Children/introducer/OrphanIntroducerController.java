package com.orphan.api.controller.manager.Children.introducer;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenDetailDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenRequest;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerDetailDto;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerDto;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.ChildrenService;
import com.orphan.common.service.OrphanIntroducerService;
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
@RequestMapping("/api/v1/manager/introducer")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_CHILDREN')")
@RequiredArgsConstructor
public class OrphanIntroducerController {

    private final OrphanIntroducerService orphanIntroducerService;

    @ApiOperation("Get All Introducers")
    @GetMapping("/all")
    public APIResponse<?> viewAllIntroducers() throws NotFoundException {
        return APIResponse.okStatus(orphanIntroducerService.viewAllIntroducers());
    }

    @ApiOperation("Get Introducers By Pages")
    @GetMapping
    public APIResponse<?> viewIntroducersByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<IntroducerDto> introducerDtoPageInfo;
        if (page != null) {
            introducerDtoPageInfo = orphanIntroducerService.viewIntroducersByPage(page, PageableConstants.limit);
        } else {
            introducerDtoPageInfo = orphanIntroducerService.viewIntroducersByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(introducerDtoPageInfo);
    }

    @ApiOperation("View Introducers Detail")
    @GetMapping("/{introducerId}")
    public APIResponse<?> viewIntroducerDetail(@PathVariable("introducerId") Integer introducerId) throws NotFoundException {
        IntroducerDetailDto introducerDetailDto = orphanIntroducerService.viewIntroducerDetail(introducerId);
        return APIResponse.okStatus(introducerDetailDto);
    }

    @ApiOperation("Create new Introducers")
    @PostMapping
    public APIResponse<?> createIntroducers(@Valid @RequestBody IntroducerRequest introducerRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        introducerRequest = orphanIntroducerService.createIntroducer(introducerRequest);
        return APIResponse.okStatus(introducerRequest);
    }

    @ApiOperation("Update introducer detail")
    @PutMapping("/{introducerId}")
    public APIResponse<?> updateIntroducers(@PathVariable("introducerId") Integer introducerId, @Valid @RequestBody IntroducerRequest introducerRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        introducerRequest = orphanIntroducerService.updateIntroducer(introducerRequest, introducerId);
        return APIResponse.okStatus(introducerRequest);
    }

    @ApiOperation("Delete Introducers")
    @DeleteMapping("/{introducerId}")
    public APIResponse<?> deleteFurniture(@PathVariable("introducerId") Integer introducerId) throws NotFoundException {
        orphanIntroducerService.deleteIntroducer(introducerId);
        return APIResponse.okStatus();
    }
}
