package com.orphan.api.controller.employee.FurnitureManager;

import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.ExtensionTimeDateDto;
import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.FurnitureRequestFormDetail;
import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.TotalMoneyDto;
import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.EmployeeFurnitureRequestFormService;
import com.orphan.common.service.FurnitureRequestFormService;
import com.orphan.common.service.FurnitureService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account/furniture/request_form")
@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
@RequiredArgsConstructor
public class EmployeeFurnitureFormController {
    private final FurnitureRequestFormService furnitureRequestFormService;
    private final EmployeeFurnitureRequestFormService employeeFurnitureRequestFormService;

    private final FurnitureService furnitureService;

    @ApiOperation("Get Furniture Request Forms By Pages")
    @GetMapping
    public APIResponse<?> viewFurnitureRequestFormsByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            , @ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<FurnitureRequestFormDetail> furnitureRequestFormDetailPageInfo;
        if (page != null) {
            furnitureRequestFormDetailPageInfo = employeeFurnitureRequestFormService.viewFurnitureRequestFormsByPage(furnitureRequestFormService.getCurrentUserId(), page, limit);
        } else {
            furnitureRequestFormDetailPageInfo = employeeFurnitureRequestFormService.viewFurnitureRequestFormsByPage(furnitureRequestFormService.getCurrentUserId(), 1, limit);

        }
        return APIResponse.okStatus(furnitureRequestFormDetailPageInfo);
    }

    @ApiOperation("View Furniture Request Form Detail")
    @GetMapping("/{furnitureRequestFormId}")
    public APIResponse<?> viewFurnitureRequestFormId(@PathVariable("furnitureRequestFormId") Integer furnitureRequestFormId) throws NotFoundException {
        FurnitureRequestFormDetail furnitureRequestFormDetail = furnitureRequestFormService.viewDetailById(furnitureRequestFormId);
        return APIResponse.okStatus(furnitureRequestFormDetail);
    }

    @ApiOperation("Update Furniture Request Status")
    @PostMapping("/status/{furnitureRequestFormId}")
    public APIResponse<?> updateStatus(@PathVariable("furnitureRequestFormId") Integer furnitureRequestFormId) throws NotFoundException {
        employeeFurnitureRequestFormService.updateFurnitureRequestFormStatus(furnitureRequestFormId);
        return APIResponse.okStatus();
    }

    @ApiOperation("Confirm Finish Form")
    @PostMapping("/confirm")
    public APIResponse<?> confirmFinishForm(@RequestBody TotalMoneyDto totalMoneyDto) throws NotFoundException {
        furnitureRequestFormService.confirmFinish(totalMoneyDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("Extend deadline Date")
    @PostMapping("/extend")
    public APIResponse<?> extendDeadlineDate(@Valid @RequestBody ExtensionTimeDateDto extensionTimeDateDto) throws NotFoundException {
        furnitureRequestFormService.extensionOfTime(extensionTimeDateDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("View Furniture Detail")
    @GetMapping("/furnitureDetail/{furnitureId}")
    public APIResponse<?> viewFurnitureDetail(@PathVariable("furnitureId") Integer furnitureId) throws NotFoundException {
        FurnitureDto furnitureDto = furnitureService.viewFurnitureDetail(furnitureId);
        return APIResponse.okStatus(furnitureDto);
    }
}
