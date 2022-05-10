package com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.FurnitureRequestFormDetail;
import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.FurnitureRequestFormDto;
import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureDto;
import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureRequest;
import com.orphan.common.entity.FurnitureRequestForm;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.FurnitureRequestFormService;
import com.orphan.common.service.FurnitureService;
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
@RequestMapping("/api/v1/manager/furniture/request_form")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC')")
@RequiredArgsConstructor
public class FurnitureRequestFormController {
    private final FurnitureRequestFormService furnitureRequestFormService;

    @ApiOperation("Get All Furniture Request Forms")
    @GetMapping("/all")
    public APIResponse<?> viewAllFurnitureRequestForms() throws NotFoundException {
        return APIResponse.okStatus(furnitureRequestFormService.viewAllFurnitureRequestForms());
    }

    @ApiOperation("Get Furniture Request Forms By Pages")
    @GetMapping
    public APIResponse<?> viewFurnitureRequestFormsByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<FurnitureRequestFormDetail> furnitureRequestFormDetailPageInfo;
        if (page != null) {
            furnitureRequestFormDetailPageInfo = furnitureRequestFormService.viewFurnitureRequestFormsByPage(page, PageableConstants.limit);
        } else {
            furnitureRequestFormDetailPageInfo = furnitureRequestFormService.viewFurnitureRequestFormsByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(furnitureRequestFormDetailPageInfo);
    }

    @ApiOperation("View Furniture Request Form Detail")
    @GetMapping("/{furnitureRequestFormId}")
    public APIResponse<?> viewFurnitureRequestFormId(@PathVariable("furnitureRequestFormId") Integer furnitureRequestFormId) throws NotFoundException {
        FurnitureRequestFormDetail furnitureRequestFormDetail = furnitureRequestFormService.viewDetailById(furnitureRequestFormId);
        return APIResponse.okStatus(furnitureRequestFormDetail);
    }

    @ApiOperation("Create new Furniture Request Form Detail")
    @PostMapping
    public APIResponse<?> createFurnitureRequestForm(@Valid @RequestBody FurnitureRequestFormDto furnitureRequestFormDto, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        FurnitureRequestFormDetail furnitureRequestFormDetail = furnitureRequestFormService.createFurnitureRequest(furnitureRequestFormDto);
        return APIResponse.okStatus(furnitureRequestFormDetail);
    }

    @ApiOperation("Delete furniture Request Form")
    @DeleteMapping("/{furnitureRequestFormId}")
    public APIResponse<?> deleteFurnitureRequestForm(@PathVariable("furnitureRequestFormId") Integer furnitureRequestFormId) throws NotFoundException {
        furnitureRequestFormService.deleteFurnitureRequestForm(furnitureRequestFormId);
        return APIResponse.okStatus();
    }

}
