package com.orphan.api.controller.manager.Logistic.Furniture.FurnitureCategory;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureCategory.dto.FurnitureCategoryDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.FurnitureCategoryService;
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
@RequestMapping("/api/v1/manager/furniture/category")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC')")
@RequiredArgsConstructor
public class FurnitureCategoryController {

    private final FurnitureCategoryService furnitureCategoryService;

    @ApiOperation("Get All Furniture Categories")
    @GetMapping("/all")
    public APIResponse<?> viewAllFurnitureCategories() throws NotFoundException {
        return APIResponse.okStatus(furnitureCategoryService.viewAllFurnitureCategories());
    }

    @ApiOperation("Get Furniture Categories By Pages")
    @GetMapping
    public APIResponse<?> viewFurnitureCategoriesByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<FurnitureCategoryDto> furnitureCategoryDtoPageInfo;
        if (page != null) {
            furnitureCategoryDtoPageInfo = furnitureCategoryService.viewFurnitureCategoriesByPage(page, PageableConstants.limit);
        } else {
            furnitureCategoryDtoPageInfo = furnitureCategoryService.viewFurnitureCategoriesByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(furnitureCategoryDtoPageInfo);
    }

    @ApiOperation("View Furniture Category Detail")
    @GetMapping("/{furnitureCategoryId}")
    public APIResponse<?> viewFurnitureDetail(@PathVariable("furnitureCategoryId") Integer furnitureCategoryId) throws NotFoundException {
        FurnitureCategoryDto furnitureCategoryDto = furnitureCategoryService.viewFurnitureCategoryDetail(furnitureCategoryId);
        return APIResponse.okStatus(furnitureCategoryDto);
    }

    @ApiOperation("Create new Furniture Category")
    @PostMapping
    public APIResponse<?> createFurnitureCategory(@Valid @RequestBody FurnitureCategoryDto furnitureCategoryDto, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        furnitureCategoryDto = furnitureCategoryService.createOrUpdateFurniture(furnitureCategoryDto);
        return APIResponse.okStatus(furnitureCategoryDto);
    }

    @ApiOperation("Update furniture category detail")
    @PutMapping("/{furnitureCategoryId}")
    public APIResponse<?> updateFurniture(@PathVariable("furnitureCategoryId") Integer furnitureCategoryId, @Valid @RequestBody FurnitureCategoryDto furnitureCategoryDto, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        furnitureCategoryDto.setFurnitureCategoryId(furnitureCategoryId);
        furnitureCategoryDto = furnitureCategoryService.createOrUpdateFurniture(furnitureCategoryDto);
        return APIResponse.okStatus(furnitureCategoryDto);
    }

    @ApiOperation("Delete furniture category")
    @DeleteMapping("/{furnitureCategoryId}")
    public APIResponse<?> deleteFurniture(@PathVariable("furnitureCategoryId") Integer furnitureCategoryId) throws NotFoundException {
        furnitureCategoryService.deleteById(furnitureCategoryId);
        return APIResponse.okStatus();
    }
}
