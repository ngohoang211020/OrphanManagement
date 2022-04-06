package com.orphan.api.controller.manager.furniture;

import com.orphan.api.controller.UpdateImageResponse;
import com.orphan.api.controller.manager.furniture.dto.FurnitureDto;
import com.orphan.api.controller.manager.furniture.dto.FurnitureRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.FurnitureService;
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
@RequestMapping("/api/v1/manager/furniture")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class FurnitureController {

    private final FurnitureService furnitureService;

    @ApiOperation("Get All Furnitures")
    @GetMapping
    public APIResponse<?> viewAllFurnitures() throws NotFoundException {
        List<FurnitureDto> furnitureDtoList = furnitureService.viewAllFurnitures();
        return APIResponse.okStatus(furnitureDtoList);
    }

    @ApiOperation("View Furniture Detail")
    @GetMapping("/{furnitureId}")
    public APIResponse<?> viewFurnitureDetail(@PathVariable("furnitureId")Integer furnitureId) throws NotFoundException {
        FurnitureDto furnitureDto = furnitureService.viewFurnitureDetail(furnitureId);
        return APIResponse.okStatus(furnitureDto);
    }

    @ApiOperation("Create new Furniture")
    @PostMapping
    public APIResponse<?> createFurniture(@Valid @RequestBody FurnitureRequest furnitureRequest) throws NotFoundException {
        furnitureRequest=furnitureService.createFurniture(furnitureRequest);
        return APIResponse.okStatus(furnitureRequest);
    }

    @ApiOperation("Update furniture detail")
    @PutMapping("/{furnitureId}")
    public APIResponse<?> updateFurniture(@PathVariable("furnitureId")Integer furnitureId, @Valid @RequestBody FurnitureRequest furnitureRequest) throws NotFoundException {
        furnitureRequest=furnitureService.updateFurniture(furnitureRequest,furnitureId);
        return APIResponse.okStatus(furnitureRequest);
    }

    @ApiOperation("Update furniture Image")
    @PostMapping("/{furnitureId}/updateImage")
    public APIResponse<?> updateFurnitureImage(@PathVariable("furnitureId")Integer furnitureId,@RequestParam("image") MultipartFile multipartFile) throws NotFoundException, IOException {
        UpdateImageResponse updateImageResponse = null;

        if (!multipartFile.isEmpty()) {
            String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            updateImageResponse = furnitureService.updateFurnitureImage(image, multipartFile.getBytes(), furnitureId);

        }
        return APIResponse.okStatus(updateImageResponse);
    }

    @ApiOperation("Delete furniture")
    @DeleteMapping("/{furnitureId}")
    public APIResponse<?> deleteFurniture(@PathVariable("furnitureId")Integer furnitureId) throws NotFoundException {
        furnitureService.deleteById(furnitureId);
        return APIResponse.okStatus();
    }

}