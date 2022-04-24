package com.orphan.api.controller.manager.Children.nurturer;


import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerDetailDto;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerDto;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerRequest;
import com.orphan.api.controller.manager.Children.nurturer.dto.NurturerDetailDto;
import com.orphan.api.controller.manager.Children.nurturer.dto.NurturerDto;
import com.orphan.api.controller.manager.Children.nurturer.dto.NurturerRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.OrphanNurturerService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.PageableConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manager/nurturer")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_CHILDREN')")
@RequiredArgsConstructor
public class OrphanNurturerController {
    private final OrphanNurturerService orphanNurturerService;

    @ApiOperation("Get All nurturers")
    @GetMapping("/all")
    public APIResponse<?> viewAllNurturers() throws NotFoundException {
        return APIResponse.okStatus(orphanNurturerService.viewAllNurturers());
    }

    @ApiOperation("Get nurturers By Pages")
    @GetMapping
    public APIResponse<?> viewNurturersByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<NurturerDto> nurturerDtoPageInfo;
        if (page != null) {
            nurturerDtoPageInfo = orphanNurturerService.viewNurturersByPage(page, PageableConstants.limit);
        } else {
            nurturerDtoPageInfo = orphanNurturerService.viewNurturersByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(nurturerDtoPageInfo);
    }

    @ApiOperation("View Nurturers Detail")
    @GetMapping("/{nurturerId}")
    public APIResponse<?> viewNurturerDetail(@PathVariable("nurturerId") Integer nurturerId) throws NotFoundException {
        NurturerDetailDto nurturerDetailDto = orphanNurturerService.viewNurturerDetail(nurturerId);
        return APIResponse.okStatus(nurturerDetailDto);
    }

    @ApiOperation("Create new Nurturers")
    @PostMapping
    public APIResponse<?> createNurturers(@Valid @RequestBody NurturerRequest nurturerRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        nurturerRequest = orphanNurturerService.createNurturer(nurturerRequest);
        return APIResponse.okStatus(nurturerRequest);
    }

    @ApiOperation("Update nurturer detail")
    @PutMapping("/{nurturerId}")
    public APIResponse<?> updateNurturers(@PathVariable("nurturerId") Integer nurturerId, @Valid @RequestBody NurturerRequest nurturerRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        nurturerRequest= orphanNurturerService.updateNurturer(nurturerRequest, nurturerId);
        return APIResponse.okStatus(nurturerRequest);
    }

    @ApiOperation("Delete Nurturers")
    @DeleteMapping("/{nurturerId}")
    public APIResponse<?> deleteFurniture(@PathVariable("nurturerId") Integer nurturerId) throws NotFoundException {
        orphanNurturerService.deleteNurturer(nurturerId);
        return APIResponse.okStatus();
    }
}
