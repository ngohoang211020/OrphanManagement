package com.orphan.api.controller.manager.staff;

import com.orphan.api.controller.UpdateImageResponse;
import com.orphan.api.controller.manager.staff.dto.StaffDetailDto;
import com.orphan.api.controller.manager.staff.dto.StaffDto;
import com.orphan.api.controller.manager.staff.dto.StaffRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.StaffService;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/staff")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @ApiOperation("Get All Staffs")
    @GetMapping
    public APIResponse<?> viewAllStaff() throws NotFoundException {
        List<StaffDto> staffDtoList = staffService.viewAllStaffs();
        return APIResponse.okStatus(staffDtoList);
    }

    @ApiOperation("View Staff Detail")
    @GetMapping("/{staffId}")
    public APIResponse<?> viewStaffDetail(@PathVariable("staffId") Integer staffId) throws NotFoundException {
        StaffDetailDto staffDetailDto = staffService.viewStaffDetail(staffId);
        return APIResponse.okStatus(staffDetailDto);
    }

    @ApiOperation("Create new Staff")
    @PostMapping
    public APIResponse<?> createStaff( @Valid @RequestBody StaffRequest staffRequest) throws BadRequestException {
        staffRequest = staffService.createStaff(staffRequest);
        return APIResponse.okStatus(staffRequest);
    }

    @ApiOperation("Update staff detail")
    @PutMapping("/{staffId}")
    public APIResponse<?> updateStaff(@PathVariable("staffId") Integer staffId, @Valid @RequestBody StaffRequest staffRequest) throws NotFoundException, BadRequestException {
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
