package com.orphan.common.service;

import com.orphan.api.controller.UpdateImageResponse;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.manager.furniture.dto.FurnitureDto;
import com.orphan.api.controller.manager.furniture.dto.FurnitureRequest;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

        furniture=furnitureRepository.save(furniture);

        furnitureRequest.setFurnitureId(furniture.getFurnitureId());

        return furnitureRequest;
    }

    public FurnitureRequest updateFurniture(FurnitureRequest furnitureRequest, Integer furnitureId) throws NotFoundException {

        Furniture furniture = findById(furnitureId);

        furniture.setFurnitureName(furnitureRequest.getNameFurniture());
        furniture.setQuantity(furnitureRequest.getQuantity());
        furniture.setStatus(furnitureRequest.getStatus().equals(FurnitureStatus.GOOD.name())  ? FurnitureStatus.GOOD : FurnitureStatus.NEED_FIX);
        furniture.setModifiedId(String.valueOf(getCurrentUserId()));

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
        PageRequest pageRequest=buildPageRequest(page,limit);
        Page<Furniture> furniturePage = furnitureRepository.findByOrderByFurnitureNameAsc(pageRequest);
        if (furniturePage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE));
        }
        List<FurnitureDto> furnitureDtoList = furniturePage.getContent().stream().map(furniture -> {
            return furnitureToFurnitureDto(furniture);
        }).collect(Collectors.toList());
        PageInfo<FurnitureDto> furnitureDtoPageInfo=new PageInfo<>();
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
        List<FurnitureDto> furnitureDtoList = furnitureList.stream().map(furniture -> {
            return furnitureToFurnitureDto(furniture);
        }).collect(Collectors.toList());
        return  furnitureDtoList;
    }

    public FurnitureDto viewFurnitureDetail(Integer furnitureId) throws NotFoundException {
        Furniture furniture = findById(furnitureId);

        return furnitureToFurnitureDto(furniture);
    }

    public UpdateImageResponse updateFurnitureImage(String image, byte[] procPic, Integer id) throws IOException {
        furnitureRepository.updateFurnitureImage(image,procPic,id);
        UpdateImageResponse updateImageResponse=new UpdateImageResponse();
        updateImageResponse.setImage(image);
        updateImageResponse.setId(id);
        updateImageResponse.setImageFile(procPic);
        return  updateImageResponse;
    }

    //mapper 
    private FurnitureDto furnitureToFurnitureDto(Furniture furniture) {
        FurnitureDto furnitureDto = new FurnitureDto();
        furnitureDto.setFurnitureId(furniture.getFurnitureId());
        furnitureDto.setImageFile(furniture.getProfPic());
        furnitureDto.setImage(furniture.getImage());
        furnitureDto.setNameFurniture(furniture.getFurnitureName());
        furnitureDto.setQuantity(furniture.getQuantity());
        furnitureDto.setStatus(furniture.getStatus().equals(FurnitureStatus.GOOD) ? FurnitureStatus.GOOD.name() : FurnitureStatus.NEED_FIX.name());
        return furnitureDto;
    }

    private Furniture furnitureRequestToFurniture(FurnitureRequest furnitureRequest) {
        Furniture furniture = new Furniture();
        furniture.setFurnitureId(furnitureRequest.getFurnitureId());
        furniture.setFurnitureName(furnitureRequest.getNameFurniture());
        furniture.setQuantity(furnitureRequest.getQuantity());
        furniture.setStatus(furnitureRequest.getStatus().equals(FurnitureStatus.GOOD.name())  ? FurnitureStatus.GOOD : FurnitureStatus.NEED_FIX);
        return furniture;
    }


}
