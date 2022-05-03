package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureCategory.dto.FurnitureCategoryDto;
import com.orphan.common.entity.FurnitureCategory;
import com.orphan.common.repository.FurnitureCategoryRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FurnitureCategoryService extends BaseService {

    private final Logger log = LoggerFactory.getLogger(FurnitureCategoryService.class);

    private final FurnitureCategoryRepository furnitureCategoryRepository;

    public FurnitureCategoryDto createOrUpdateFurniture(FurnitureCategoryDto furnitureCategoryDto) {
        FurnitureCategory furnitureCategory = new FurnitureCategory();
        furnitureCategory.setFurnitureCategoryId(furnitureCategoryDto.getFurnitureCategoryId());
        furnitureCategory.setCategoryName(furnitureCategoryDto.getCategoryName());
        if (furnitureCategoryDto.getFurnitureCategoryId() != null && furnitureCategoryDto.getFurnitureCategoryId() != 0) {
            furnitureCategory.setCreatedId(String.valueOf(getCurrentUserId()));
        } else {
            furnitureCategory.setModifiedId(String.valueOf(getCurrentUserId()));
        }
        furnitureCategory=furnitureCategoryRepository.save(furnitureCategory);
        furnitureCategoryDto.setFurnitureCategoryId(furnitureCategory.getFurnitureCategoryId());
        return furnitureCategoryDto;
    }

    public FurnitureCategory findById(Integer furnitureId) throws NotFoundException {
        Optional<FurnitureCategory> furnitureCategory = furnitureCategoryRepository.findById(furnitureId);
        if (!furnitureCategory.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_CATEGORY_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_CATEGORY));
        }
        return furnitureCategory.get();
    }

    public void deleteById(Integer furnitureCategoryId) throws NotFoundException {
        if (!furnitureCategoryRepository.findById(furnitureCategoryId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_CATEGORY_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_CATEGORY));
        }
        furnitureCategoryRepository.deleteById(furnitureCategoryId);
    }

    public PageInfo<FurnitureCategoryDto> viewFurnitureCategoriesByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<FurnitureCategory> categoryPage = furnitureCategoryRepository.findByOrderByCategoryNameAsc(pageRequest);
        if (categoryPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_CATEGORY_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_CATEGORY));
        }
        List<FurnitureCategoryDto> furnitureDtoList = categoryPage.getContent().stream().map(furnitureCategory -> FurnitureCategoryToFurnitureCategoryDto(furnitureCategory)).collect(Collectors.toList());
        PageInfo<FurnitureCategoryDto> furnitureDtoPageInfo = new PageInfo<>();
        furnitureDtoPageInfo.setPage(page);
        furnitureDtoPageInfo.setLimit(limit);
        furnitureDtoPageInfo.setResult(furnitureDtoList);
        furnitureDtoPageInfo.setTotal(categoryPage.getTotalElements());
        furnitureDtoPageInfo.setPages(categoryPage.getTotalPages());
        return furnitureDtoPageInfo;
    }

    public List<FurnitureCategoryDto> viewAllFurnitureCategories() throws NotFoundException {
        List<FurnitureCategory> furnitureCategoryList = furnitureCategoryRepository.findAll();
        if (furnitureCategoryList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_CATEGORY_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_CATEGORY));
        }
        List<FurnitureCategoryDto> furnitureCategoryDtoList = furnitureCategoryList.stream().map(furnitureCategory -> FurnitureCategoryToFurnitureCategoryDto(furnitureCategory)).collect(Collectors.toList());
        return furnitureCategoryDtoList;
    }

    public FurnitureCategoryDto viewFurnitureCategoryDetail(Integer furnitureCategoryId) throws NotFoundException {
        FurnitureCategory furnitureCategory = findById(furnitureCategoryId);
        return FurnitureCategoryToFurnitureCategoryDto(furnitureCategory);
    }


    //MAPPER
    public FurnitureCategoryDto FurnitureCategoryToFurnitureCategoryDto(FurnitureCategory furnitureCategory) {
        FurnitureCategoryDto furnitureCategoryDto = new FurnitureCategoryDto();
        furnitureCategoryDto.setFurnitureCategoryId(furnitureCategory.getFurnitureCategoryId());
        furnitureCategoryDto.setCategoryName(furnitureCategory.getCategoryName());
        return furnitureCategoryDto;
    }

    public FurnitureCategory FurnitureCategoryDtoToFurnitureCategory(FurnitureCategoryDto furnitureCategoryDto) {
        FurnitureCategory furnitureCategory = new FurnitureCategory();
        furnitureCategory.setFurnitureCategoryId(furnitureCategoryDto.getFurnitureCategoryId());
        furnitureCategory.setCategoryName(furnitureCategoryDto.getCategoryName());
        return furnitureCategory;
    }
}
