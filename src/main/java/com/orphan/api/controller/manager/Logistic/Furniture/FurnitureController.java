package com.orphan.api.controller.manager.Logistic.Furniture;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureDto;
import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureRequest;
import com.orphan.common.response.APIResponse;
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
@RequestMapping("/api/v1/manager/furniture")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC')")
@RequiredArgsConstructor
public class FurnitureController {

    private final FurnitureService furnitureService;

    @ApiOperation("Get All Furnitures")
    @GetMapping("/all")
    public APIResponse<?> viewAllFurnitures() throws NotFoundException {
        return APIResponse.okStatus(furnitureService.viewAllFurnitures());
    }

    @ApiOperation("Get Furnitures By Pages")
    @GetMapping
    public APIResponse<?> viewFurnituresByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<FurnitureDto> furnitureDtoPageInfo;
        if (page != null) {
            furnitureDtoPageInfo = furnitureService.viewFurnituresByPage(page, PageableConstants.limit);
        } else {
            furnitureDtoPageInfo = furnitureService.viewFurnituresByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(furnitureDtoPageInfo);
    }

    @ApiOperation("View Furniture Detail")
    @GetMapping("/{furnitureId}")
    public APIResponse<?> viewFurnitureDetail(@PathVariable("furnitureId") Integer furnitureId) throws NotFoundException {
        FurnitureDto furnitureDto = furnitureService.viewFurnitureDetail(furnitureId);
        return APIResponse.okStatus(furnitureDto);
    }

    @ApiOperation("Create new Furniture")
    @PostMapping
    public APIResponse<?> createFurniture(@Valid @RequestBody FurnitureRequest furnitureRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        furnitureRequest = furnitureService.createFurniture(furnitureRequest);
        return APIResponse.okStatus(furnitureRequest);
    }

    @ApiOperation("Update furniture detail")
    @PutMapping("/{furnitureId}")
    public APIResponse<?> updateFurniture(@PathVariable("furnitureId") Integer furnitureId, @Valid @RequestBody FurnitureRequest furnitureRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        furnitureRequest = furnitureService.updateFurniture(furnitureRequest, furnitureId);
        return APIResponse.okStatus(furnitureRequest);
    }

    @ApiOperation("Delete furniture")
    @DeleteMapping("/{furnitureId}")
    public APIResponse<?> deleteFurniture(@PathVariable("furnitureId") Integer furnitureId) throws NotFoundException {
        furnitureService.deleteById(furnitureId);
        return APIResponse.okStatus();
    }

}
