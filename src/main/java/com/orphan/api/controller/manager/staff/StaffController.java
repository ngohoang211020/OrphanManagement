package com.orphan.api.controller.manager.staff;

import com.google.gson.JsonObject;
import com.orphan.api.controller.UpdateImageResponse;
import com.orphan.api.controller.manager.staff.dto.StaffDetailDto;
import com.orphan.api.controller.manager.staff.dto.StaffDto;
import com.orphan.api.controller.manager.staff.dto.StaffRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.StaffService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.PageableConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/manager/staff")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @ApiOperation("Get Staff By Pages")
    @GetMapping
    public APIResponse<?> viewStaffsByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
                                           ) throws NotFoundException {
        PageInfo<StaffDto> staffDtoPageInfo;
        if (page != null) {
            staffDtoPageInfo = staffService.viewAllStaffs(page, PageableConstants.limit);
        } else {
            staffDtoPageInfo = staffService.viewAllStaffs(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(staffDtoPageInfo);
    }

    @ApiOperation("View Staff Detail")
    @GetMapping("/{staffId}")
    public APIResponse<?> viewStaffDetail(@PathVariable("staffId") Integer staffId) throws NotFoundException {
        StaffDetailDto staffDetailDto = staffService.viewStaffDetail(staffId);
        return APIResponse.okStatus(staffDetailDto);
    }

    @ApiOperation("Create new Staff")
    @PostMapping
    public APIResponse<?> createStaff(@Valid @RequestBody StaffRequest staffRequest, Errors errors) throws BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        staffRequest = staffService.createStaff(staffRequest);
        return APIResponse.okStatus(staffRequest);
    }

    @ApiOperation("Update staff detail")
    @PutMapping("/{staffId}")
    public APIResponse<?> updateStaff(@PathVariable("staffId") Integer staffId, @Valid @RequestBody StaffRequest staffRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        staffRequest = staffService.updateStaff(staffRequest, staffId);
        return APIResponse.okStatus(staffRequest);
    }

    @ApiOperation("Update Staff Image")
    @PostMapping("/{staffId}/updateImage")
    public APIResponse<?> updateFurnitureImage(@PathVariable("staffId") Integer staffId, @RequestParam("image") MultipartFile multipartFile) throws NotFoundException, IOException {
        UpdateImageResponse updateImageResponse = null;

        if (!multipartFile.isEmpty()) {
            String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            updateImageResponse = staffService.updateStaffImage(image, multipartFile.getBytes(), staffId);

        }
        return APIResponse.okStatus(updateImageResponse);
    }

    @ApiOperation("Delete Staff")
    @DeleteMapping("/{staffId}")
    public APIResponse<?> deleteFurniture(@PathVariable("staffId") Integer staffId) throws NotFoundException {
        staffService.deleteById(staffId);
        return APIResponse.okStatus();
    }
}
