package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureDto;
import com.orphan.api.controller.manager.Logistic.Furniture.dto.FurnitureRequest;
import com.orphan.common.entity.Furniture;
import com.orphan.common.repository.FurnitureRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.FurnitureStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FurnitureService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(FurnitureService.class);

    private final FurnitureRepository furnitureRepository;

    public Furniture findById(Integer furnitureId) throws NotFoundException {
        Optional<Furniture> furniture = furnitureRepository.findById(furnitureId);
        if (!furniture.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE));
        }
        return furniture.get();
    }

    public FurnitureRequest createFurniture(FurnitureRequest furnitureRequest) {
        Furniture furniture = furnitureRequestToFurniture(furnitureRequest);
        furniture.setCreatedId(String.valueOf(getCurrentUserId()));

        furniture = this.furnitureRepository.save(furniture);

        furnitureRequest.setFurnitureId(furniture.getFurnitureId());


        return furnitureRequest;
    }

    public FurnitureRequest updateFurniture(FurnitureRequest furnitureRequest, Integer furnitureId) throws NotFoundException {

        Furniture furniture = findById(furnitureId);

        furniture.setFurnitureName(furnitureRequest.getNameFurniture());
        furniture.setGoodQuantity(furnitureRequest.getGoodQuantity());
        furniture.setModifiedId(String.valueOf(getCurrentUserId()));
        furniture.setUnitPrice(furnitureRequest.getUnitPrice());
        furniture.setBrokenQuantity(furnitureRequest.getBrokenQuantity());
        if (furnitureRequest.getImage() != "" && furnitureRequest.getImage() != null) {
            furniture.setImage(furnitureRequest.getImage());
        }

        this.furnitureRepository.save(furniture);

        furnitureRequest.setFurnitureId(furnitureId);

        return furnitureRequest;
    }

    public void deleteById(Integer furnitureId) throws NotFoundException {
        if (!furnitureRepository.findById(furnitureId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE));
        }
        furnitureRepository.deleteById(furnitureId);
    }

    public PageInfo<FurnitureDto> viewFurnituresByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<Furniture> furniturePage = furnitureRepository.findByOrderByCreatedAtAsc(pageRequest);
        if (furniturePage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE));
        }
        List<FurnitureDto> furnitureDtoList = furniturePage.getContent().stream().map(furniture -> furnitureToFurnitureDto(furniture)).collect(Collectors.toList());
        PageInfo<FurnitureDto> furnitureDtoPageInfo = new PageInfo<>();
        furnitureDtoPageInfo.setPage(page);
        furnitureDtoPageInfo.setLimit(limit);
        furnitureDtoPageInfo.setResult(furnitureDtoList);
        furnitureDtoPageInfo.setTotal(furniturePage.getTotalElements());
        furnitureDtoPageInfo.setPages(furniturePage.getTotalPages());
        return furnitureDtoPageInfo;
    }

    public List<FurnitureDto> viewAllFurnitures() throws NotFoundException {
        List<Furniture> furnitureList = furnitureRepository.findAll();
        if (furnitureList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE));
        }
        List<FurnitureDto> furnitureDtoList = furnitureList.stream().map(furniture -> furnitureToFurnitureDto(furniture)).collect(Collectors.toList());
        return furnitureDtoList;
    }

    public FurnitureDto viewFurnitureDetail(Integer furnitureId) throws NotFoundException {
        Furniture furniture = findById(furnitureId);
        return furnitureToFurnitureDto(furniture);
    }

    //mapper 
    private FurnitureDto furnitureToFurnitureDto(Furniture furniture) {
        FurnitureDto furnitureDto = new FurnitureDto();
        furnitureDto.setFurnitureId(furniture.getFurnitureId());
        furnitureDto.setImage(furniture.getImage());
        furnitureDto.setNameFurniture(furniture.getFurnitureName());
        furnitureDto.setGoodQuantity(furniture.getGoodQuantity());
        furnitureDto.setUnitPrice(furniture.getUnitPrice());
        furnitureDto.setBrokenQuantity(furniture.getBrokenQuantity());
        return furnitureDto;
    }

    private Furniture furnitureRequestToFurniture(FurnitureRequest furnitureRequest) {
        Furniture furniture = new Furniture();
        furniture.setFurnitureId(furnitureRequest.getFurnitureId());
        if (furnitureRequest.getImage() != null && furnitureRequest.getImage() != "") {
            furniture.setImage(furnitureRequest.getImage());
        }
        furniture.setUnitPrice(furnitureRequest.getUnitPrice());
        furniture.setBrokenQuantity(furnitureRequest.getBrokenQuantity());
        furniture.setFurnitureName(furnitureRequest.getNameFurniture());
        furniture.setGoodQuantity(furnitureRequest.getGoodQuantity());

        return furniture;
    }

//    private String furnitureStatusToString(String furnitureStatus) {
//        return furnitureStatus.equals(FurnitureStatus.GOOD.toString()) ? FurnitureStatus.GOOD.toString() : FurnitureStatus.NEED_FIX.getCode();
//    }

}
