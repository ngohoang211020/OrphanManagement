package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.FurnitureRequestFormDetail;
import com.orphan.common.entity.FurnitureRequestForm;
import com.orphan.common.repository.FurnitureRequestFormRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.RequestStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeFurnitureRequestFormService extends BaseService {
    private final FurnitureRequestFormRepository furnitureRequestFormRepository;

    private final FurnitureRequestFormService furnitureRequestFormService;

    public PageInfo<FurnitureRequestFormDetail> viewFurnitureRequestFormsByPage(Integer userId,Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<FurnitureRequestForm> furnitureRequestFormPage = furnitureRequestFormRepository.findByUser_LoginIdOrderByStartDateAsc(userId,pageRequest);
        if (furnitureRequestFormPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
        List<FurnitureRequestFormDetail> furnitureRequestFormDetails =furnitureRequestFormService.toListFormDetailDto(furnitureRequestFormPage.getContent());
        PageInfo<FurnitureRequestFormDetail> furnitureRequestFormDetailPageInfo = new PageInfo<>();
        furnitureRequestFormDetailPageInfo.setPage(page);
        furnitureRequestFormDetailPageInfo.setLimit(limit);
        furnitureRequestFormDetailPageInfo.setResult(furnitureRequestFormDetails);
        furnitureRequestFormDetailPageInfo.setTotal(furnitureRequestFormPage.getTotalElements());
        furnitureRequestFormDetailPageInfo.setPages(furnitureRequestFormPage.getTotalPages());
        return furnitureRequestFormDetailPageInfo;
    }

    public void updateFurnitureRequestFormStatus(Integer furnitureRequestFormId) throws NotFoundException {
        if(furnitureRequestFormRepository.existsByFurnitureRequestId(furnitureRequestFormId)){
            if(furnitureRequestFormRepository.findById(furnitureRequestFormId).get().getStatus().equals(RequestStatus.DONE)){
                return ;
            }
            else{
                furnitureRequestFormRepository.updateStatusByStatus(RequestStatus.DONE.getCode(),furnitureRequestFormId);
            }
        }
        else {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
    }


}
