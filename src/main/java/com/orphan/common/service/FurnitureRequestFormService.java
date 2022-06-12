package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto.*;
import com.orphan.common.entity.Furniture;
import com.orphan.common.entity.FurnitureRequestForm;
import com.orphan.common.entity.SpecifyFurnitureRequest;
import com.orphan.common.entity.User;
import com.orphan.common.repository.FundManagementRepository;
import com.orphan.common.repository.FurnitureRepository;
import com.orphan.common.repository.FurnitureRequestFormRepository;
import com.orphan.common.repository.SpecifyFurnitureRequestRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.RequestStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FurnitureRequestFormService extends BaseService {
    private final FurnitureRequestFormRepository furnitureRequestFormRepository;

    private final FurnitureRepository furnitureRepository;

    private final FurnitureService furnitureService;

    private final SpecifyFurnitureRequestRepository specifyFurnitureRequestRepository;

    private final UserService userService;

    private final FundManagementRepository fundManagementRepository;

    public FurnitureRequestForm findById(Integer furnitureRequestId) throws NotFoundException {
        Optional<FurnitureRequestForm> furnitureRequestFormOptional = furnitureRequestFormRepository.findById(furnitureRequestId);
        if (!furnitureRequestFormOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
        return furnitureRequestFormOptional.get();
    }

    public FurnitureRequestFormDetail viewDetailById(Integer furnitureRequestId) throws NotFoundException {
        return toDto(findById(furnitureRequestId));
    }

    public FurnitureRequestFormDetail createFurnitureRequest(FurnitureRequestFormDto furnitureRequestFormDto) throws NotFoundException {
        FurnitureRequestForm furnitureRequestForm = new FurnitureRequestForm();
        User user = userService.findById(furnitureRequestFormDto.getEmployeeId());
        furnitureRequestForm.setUser(user);

        long now = (new Date()).getTime();
        long dateToMilliseconds = 60 * 60 * 1000;
        furnitureRequestForm.setStartDate(new Date(now));

        furnitureRequestForm.setDeadlineDate(new Date(now + 4 * 24 * dateToMilliseconds));

        furnitureRequestForm.setStatus(RequestStatus.IN_PROGRESS.getCode());

        furnitureRequestForm.setCreatedId(String.valueOf(getCurrentUserId()));

        List<SpecifyFurnitureRequest> specifyFurnitureRequestList = toListEntity(furnitureRequestFormDto.getFurnitureRequestList(), furnitureRequestForm);

        furnitureRequestForm.setSpecifyFurnitureRequestList(specifyFurnitureRequestList);

        this.furnitureRequestFormRepository.save(furnitureRequestForm);

        specifyFurnitureRequestRepository.saveAll(specifyFurnitureRequestList);

        furnitureRequestFormDto.setFurnitureRequestId(furnitureRequestFormDto.getFurnitureRequestId());

        return toDto(furnitureRequestForm);
    }

    public void deleteFurnitureRequestForm(Integer furnitureRequestId) throws NotFoundException {

        FurnitureRequestForm furnitureRequestForm = findById(furnitureRequestId);
        if (furnitureRequestForm.getStatus().equals(RequestStatus.DONE.getCode())) {
            furnitureRequestFormRepository.deleteById(furnitureRequestId);
        } else {
            long startDate = (furnitureRequestForm.getStartDate()).getTime();
            long now = (new Date()).getTime();
            long dateToMilliseconds = 60 * 60 * 1000;
            if (now > startDate + 1.5 * dateToMilliseconds) {
                furnitureRequestFormRepository.deleteById(furnitureRequestId);
            }
        }
    }

//    public void updateDoneStatus() {
//        furnitureRequestFormRepository.updateStatusByStatusAndFinishDate(RequestStatus.DONE.getCode(), RequestStatus.IN_PROGRESS.getCode(), new Date(new Date().getTime()));
//    }

    public List<FurnitureRequestFormDetail> viewAllFurnitureRequestForms() throws NotFoundException {
        List<FurnitureRequestForm> furnitureRequestForms = furnitureRequestFormRepository.findAll();
        if (furnitureRequestForms.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
        List<FurnitureRequestFormDetail> furnitureRequestFormDetails = toListFormDetailDto(furnitureRequestForms);
        return furnitureRequestFormDetails;
    }

    public PageInfo<FurnitureRequestFormDetail> viewFurnitureRequestFormsByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("startDate").descending());
        Page<FurnitureRequestForm> furnitureRequestFormPage = furnitureRequestFormRepository.findByOrderByCreatedAtAsc(pageRequest);
        if (furnitureRequestFormPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
        List<FurnitureRequestFormDetail> furnitureRequestFormDetails = toListFormDetailDto(furnitureRequestFormPage.getContent());
        PageInfo<FurnitureRequestFormDetail> furnitureRequestFormDetailPageInfo = new PageInfo<>();
        furnitureRequestFormDetailPageInfo.setPage(page);
        furnitureRequestFormDetailPageInfo.setLimit(limit);
        furnitureRequestFormDetailPageInfo.setResult(furnitureRequestFormDetails);
        furnitureRequestFormDetailPageInfo.setTotal(furnitureRequestFormPage.getTotalElements());
        furnitureRequestFormDetailPageInfo.setPages(furnitureRequestFormPage.getTotalPages());
        return furnitureRequestFormDetailPageInfo;
    }

//    public void updateAutoFurnitureForm() {
//
//        furnitureRequestFormRepository.updateStatusByStatusAndFinishDate(RequestStatus.DONE.getCode(), RequestStatus.IN_PROGRESS.getCode(), new Date(new Date().getTime()));
//        List<SpecifyFurnitureRequest> specifyFurnitureRequestList1 = specifyFurnitureRequestRepository.findByStatus(false);
//        for (SpecifyFurnitureRequest specifyFurnitureRequest : specifyFurnitureRequestList1
//        ) {
//            Furniture furniture = specifyFurnitureRequest.getFurniture();
//            Integer goodQuantity = furniture.getGoodQuantity() + specifyFurnitureRequest.getImportQuantity() + specifyFurnitureRequest.getFixQuantity();
//            Integer brokenQuantity = furniture.getBrokenQuantity() - specifyFurnitureRequest.getFixQuantity();
//            furniture.setGoodQuantity(goodQuantity);
//            furniture.setBrokenQuantity(brokenQuantity);
//            furnitureRepository.save(furniture);
//            specifyFurnitureRequestRepository.save(specifyFurnitureRequest);
//        }
//
//        List<SpecifyFurnitureRequest> specifyFurnitureRequestList = specifyFurnitureRequestRepository.findByStatusAndFurnitureRequestForm_StatusAndFurnitureRequestForm_FinishDate(false, RequestStatus.DONE.getCode(), new Date(new Date().getTime()));
//
//        for (SpecifyFurnitureRequest specifyFurnitureRequest : specifyFurnitureRequestList
//        ) {
//            Furniture furniture = specifyFurnitureRequest.getFurniture();
//            Integer goodQuantity = furniture.getGoodQuantity() + specifyFurnitureRequest.getImportQuantity() + specifyFurnitureRequest.getFixQuantity();
//            Integer brokenQuantity = furniture.getBrokenQuantity() - specifyFurnitureRequest.getFixQuantity();
//            furniture.setGoodQuantity(goodQuantity);
//            furniture.setBrokenQuantity(brokenQuantity);
//            furnitureRepository.save(furniture);
//            specifyFurnitureRequestRepository.save(specifyFurnitureRequest);
//        }
//    }

    public void confirmFinish(TotalMoneyDto totalMoneyDto) throws NotFoundException {
        Optional<FurnitureRequestForm> furnitureRequestFormOptional = furnitureRequestFormRepository.findById(totalMoneyDto.getId());
        if (!furnitureRequestFormOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
        furnitureRequestFormRepository.confirmFinish(RequestStatus.DONE.getCode(), new Date(new Date().getTime()), totalMoneyDto.getTotalMoney(), totalMoneyDto.getId());
    }

    public void extensionOfTime(ExtensionTimeDateDto extensionTimeDateDto) throws NotFoundException {
        Optional<FurnitureRequestForm> furnitureRequestFormOptional = furnitureRequestFormRepository.findById(extensionTimeDateDto.getId());
        if (!furnitureRequestFormOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.FURNITURE_REQUEST_FORM));
        }
        furnitureRequestFormRepository.extensionOfTime(OrphanUtils.StringToDate(extensionTimeDateDto.getExtensionDate()), RequestStatus.IN_PROGRESS.getCode(), extensionTimeDateDto.getId());
    }
//    public void updateFundInFundOut() {
//        FundManagement fundManagement = fundManagementRepository.findByDate(new Date(new Date().getTime()));
//        Long outMoney = null;
//        Long inMoney;
//        if (fundManagement == null) {
//            fundManagement = new FundManagement();
//            outMoney = Long.valueOf(0);
//        } else {
//            outMoney = fundManagement.getOutMoney();
//        }
//        List<FurnitureRequestForm> furnitureRequestForms = furnitureRequestFormRepository.findByStartDateAndStatus(new Date(new Date().getTime()), "DONE");
//        List<FurnitureRequestFormDetail> furnitureRequestFormDetails = toListFormDetailDto(furnitureRequestForms);
//        for (FurnitureRequestFormDetail furnitureRequestFormDetail : furnitureRequestFormDetails) {
//            fundManagement.setDate(OrphanUtils.StringToDate(furnitureRequestFormDetail.getFinishDate()));
//            outMoney -= furnitureRequestFormDetail.getTotalPrice();
//            fundManagement.setDescription("IMPORT/FIX Furniture");
//        }
//        fundManagement.setOutMoney(outMoney);
//        fundManagementRepository.save(fundManagement);
//    }

    //mapper
    private SpecifyFurnitureRequest toEntity(SpecifyFurnitureRequestDto specifyFurnitureRequestDto, FurnitureRequestForm furnitureRequestForm) throws NotFoundException {
        SpecifyFurnitureRequest specifyFurnitureRequest = new SpecifyFurnitureRequest();
        Furniture furniture = furnitureService.findById(specifyFurnitureRequestDto.getFurnitureId());
        furniture.setFurnitureId(furniture.getFurnitureId());
        specifyFurnitureRequest.setFurniture(furniture);
        specifyFurnitureRequest.setFixQuantity(specifyFurnitureRequestDto.getFixQuantity());
        specifyFurnitureRequest.setImportQuantity(specifyFurnitureRequestDto.getImportQuantity());
        specifyFurnitureRequest.setNote(specifyFurnitureRequestDto.getNote());
        specifyFurnitureRequest.setSpecifyFurnitureRequestId(specifyFurnitureRequestDto.getSpecifyFurnitureRequestId());
        specifyFurnitureRequest.setFurnitureRequestForm(furnitureRequestForm);
        return specifyFurnitureRequest;
    }

    private List<SpecifyFurnitureRequest> toListEntity(List<SpecifyFurnitureRequestDto> specifyFurnitureRequestDtoList, FurnitureRequestForm furnitureRequestForm) {
        List<SpecifyFurnitureRequest> specifyFurnitureRequestList;
        specifyFurnitureRequestList = specifyFurnitureRequestDtoList.stream().map(specifyFurnitureRequestDto ->
        {
            try {
                return toEntity(specifyFurnitureRequestDto, furnitureRequestForm);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return specifyFurnitureRequestList;
    }

//    public List<SpecifyFurnitureRequest> toListEntity(List<SpecifyFurnitureRequestDto> specifyFurnitureRequestDtoList) {
//        List<SpecifyFurnitureRequest> specifyFurnitureRequestList = new ArrayList<>();
//        specifyFurnitureRequestList = specifyFurnitureRequestDtoList.stream().map(specifyFurnitureRequestDto ->
//        {
//            try {
//                return toEntity(specifyFurnitureRequestDto, null);
//            } catch (NotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }).collect(Collectors.toList());
//        return specifyFurnitureRequestList;
//    }

    private SpecifyFurnitureRequestDto toDto(SpecifyFurnitureRequest specifyFurnitureRequest) {
        SpecifyFurnitureRequestDto specifyFurnitureRequestDto = new SpecifyFurnitureRequestDto();
        specifyFurnitureRequestDto.setFurnitureId(specifyFurnitureRequest.getFurniture().getFurnitureId());
        specifyFurnitureRequestDto.setSpecifyFurnitureRequestId(specifyFurnitureRequest.getSpecifyFurnitureRequestId());
        specifyFurnitureRequestDto.setFixQuantity(specifyFurnitureRequest.getFixQuantity());
        specifyFurnitureRequestDto.setImportQuantity(specifyFurnitureRequest.getImportQuantity());
        specifyFurnitureRequestDto.setNote(specifyFurnitureRequest.getNote());
        return specifyFurnitureRequestDto;
    }

    public List<SpecifyFurnitureRequestDto> toListDto(List<SpecifyFurnitureRequest> specifyFurnitureRequestList) {
        List<SpecifyFurnitureRequestDto> specifyFurnitureRequestDtoList;
        specifyFurnitureRequestDtoList = specifyFurnitureRequestList.stream().map(specifyFurnitureRequest ->
                toDto(specifyFurnitureRequest)).collect(Collectors.toList());
        return specifyFurnitureRequestDtoList;
    }

    private FurnitureRequestFormDetail toDto(FurnitureRequestForm furnitureRequestForm) {
        FurnitureRequestFormDetail furnitureRequestFormDetail = new FurnitureRequestFormDetail();

        furnitureRequestFormDetail.setFurnitureRequestList(toListDto(furnitureRequestForm.getSpecifyFurnitureRequestList()));
        furnitureRequestFormDetail.setFurnitureRequestId(furnitureRequestForm.getFurnitureRequestId());
        furnitureRequestFormDetail.setEmployeeId(furnitureRequestForm.getUser().getLoginId());
        furnitureRequestFormDetail.setEmployeeName(furnitureRequestForm.getUser().getFullName());
        furnitureRequestFormDetail.setTotalPrice(furnitureRequestForm.getTotalPrice());

        furnitureRequestFormDetail.setStartDate(OrphanUtils.DateToString(furnitureRequestForm.getStartDate()));
        if (furnitureRequestForm.getFinishDate()!=null) {
            furnitureRequestFormDetail.setFinishDate(OrphanUtils.DateToString(furnitureRequestForm.getFinishDate()));
        }
        furnitureRequestFormDetail.setDeadlineDate(OrphanUtils.DateToString(furnitureRequestForm.getDeadlineDate()));
        furnitureRequestFormDetail.setStatus(furnitureRequestForm.getStatus());
        furnitureRequestFormDetail.setCreatedId(Integer.valueOf(furnitureRequestForm.getCreatedId()));
        return furnitureRequestFormDetail;
    }

    List<FurnitureRequestFormDetail> toListFormDetailDto(List<FurnitureRequestForm> furnitureRequestFormList) {
        List<FurnitureRequestFormDetail> furnitureRequestFormDetails = new ArrayList<>();
        furnitureRequestFormDetails = furnitureRequestFormList.stream().map(furnitureRequestForm ->
                toDto(furnitureRequestForm)).collect(Collectors.toList());
        return furnitureRequestFormDetails;
    }
}
