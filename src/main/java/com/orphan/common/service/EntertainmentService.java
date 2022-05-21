package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.Entertainment.dto.EntertainmentDto;
import com.orphan.api.controller.manager.Logistic.Entertainment.dto.EntertainmentRequest;
import com.orphan.common.entity.Entertainment;
import com.orphan.common.repository.EntertainmentRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
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
public class EntertainmentService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(EntertainmentService.class);

    private final EntertainmentRepository entertainmentRepository;

    public Entertainment findById(Integer entertainmentId) throws NotFoundException {
        Optional<Entertainment> entertainment = entertainmentRepository.findById(entertainmentId);
        if (!entertainment.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_ENTERTAINMENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.ENTERTAINMENT));
        }
        return entertainment.get();
    }

    public EntertainmentRequest createEntertainment(EntertainmentRequest entertainmentRequest) {
        Entertainment entertainment = entertainmentRequestToEntertainment(entertainmentRequest);
        entertainment.setCreatedId(String.valueOf(getCurrentUserId()));

        entertainment=this.entertainmentRepository.save(entertainment);

        entertainmentRequest.setIdEntertainment(entertainment.getIdEntertainment());

        return entertainmentRequest;
    }

    public EntertainmentRequest updateEntertainment(EntertainmentRequest entertainmentRequest, Integer entertainmentId) throws NotFoundException {

        Entertainment entertainment = findById(entertainmentId);

        entertainment.setNameEntertainment(entertainmentRequest.getNameEntertainment());
        entertainment.setModifiedId(String.valueOf(getCurrentUserId()));

        if(entertainmentRequest.getImage()!=""){
            entertainment.setNameEntertainment(entertainmentRequest.getImage());
            entertainmentRequest.setImage(entertainment.getImage());
        }
        this.entertainmentRepository.save(entertainment);

        entertainment.setAddress(entertainmentRequest.getAddress());



        entertainmentRequest.setIdEntertainment(entertainmentId);

        return entertainmentRequest;
    }

    public void deleteById(Integer entertainmentId) throws NotFoundException {
        if (!entertainmentRepository.findById(entertainmentId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_ENTERTAINMENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.ENTERTAINMENT));
        }
        entertainmentRepository.deleteById(entertainmentId);
    }

    public PageInfo<EntertainmentDto> viewEntertainmentsByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest=buildPageRequest(page,limit);
        Page<Entertainment> entertainmentPage = entertainmentRepository.findByOrderByEntertainmentAsc(pageRequest);
        if (entertainmentPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_ENTERTAINMENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.ENTERTAINMENT));
        }
        List<EntertainmentDto> entertainmentDtoList = entertainmentPage.getContent().stream().map(entertainment -> entertainmentToEntertainmentDto(entertainment)).collect(Collectors.toList());
        PageInfo<EntertainmentDto> entertainmentDtoPageInfo=new PageInfo<>();
        entertainmentDtoPageInfo.setPage(page);
        entertainmentDtoPageInfo.setLimit(limit);
        entertainmentDtoPageInfo.setResult(entertainmentDtoList);
        entertainmentDtoPageInfo.setTotal(entertainmentPage.getTotalElements());
        entertainmentDtoPageInfo.setPages(entertainmentPage.getTotalPages());
        return entertainmentDtoPageInfo;
    }

    public List<EntertainmentDto> viewAllEntertainments() throws NotFoundException {
        List<Entertainment> entertainmentList = entertainmentRepository.findAll();
        if (entertainmentList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_ENTERTAINMENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.ENTERTAINMENT));
        }
        List<EntertainmentDto> entertainmentDtoList = entertainmentList.stream().map(entertainment -> entertainmentToEntertainmentDto(entertainment)).collect(Collectors.toList());
        return  entertainmentDtoList;
    }

    public EntertainmentDto viewEntertainmentDetail(Integer furnitureId) throws NotFoundException {
        Entertainment entertainment = findById(furnitureId);
        return entertainmentToEntertainmentDto(entertainment);
    }


    //mapper
    private EntertainmentDto entertainmentToEntertainmentDto(Entertainment entertainment) {
        EntertainmentDto entertainmentDto = new EntertainmentDto();
        entertainmentDto.setIdEntertainment(entertainment.getIdEntertainment());
        entertainmentDto.setNameEntertainment(entertainment.getNameEntertainment());

        entertainmentDto.setDateOfEntertainment(OrphanUtils.DateToString(entertainment.getDateOfEntertainment()));

        entertainmentDto.setAddress(entertainment.getAddress());
        entertainmentDto.setImage(entertainment.getImage());
        return entertainmentDto;
    }

    private Entertainment entertainmentRequestToEntertainment(EntertainmentRequest entertainmentRequest) {
        Entertainment entertainment = new Entertainment();
        entertainment.setIdEntertainment(entertainmentRequest.getIdEntertainment());
        entertainment.setNameEntertainment(entertainmentRequest.getNameEntertainment());

        entertainment.setDateOfEntertainment(OrphanUtils.StringToDate(entertainmentRequest.getDateOfEntertainment()));

        entertainment.setAddress(entertainmentRequest.getAddress());
        entertainment.setImage(entertainmentRequest.getImage());
        return entertainment;
    }




}
