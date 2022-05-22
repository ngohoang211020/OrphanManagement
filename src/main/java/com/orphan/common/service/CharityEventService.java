package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.*;
import com.orphan.common.entity.Benefactor;
import com.orphan.common.entity.BenefactorCharity;
import com.orphan.common.entity.CharityEvent;
import com.orphan.common.repository.BenefactorCharityRepository;
import com.orphan.common.repository.BenefactorRepository;
import com.orphan.common.repository.CharityEventRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class CharityEventService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(CharityEventService.class);
    private final CharityEventRepository charityEventRepository;
    private final BenefactorRepository benefactorRepository;

    private final BenefactorCharityRepository benefactorCharityRepository;

    private final FurnitureRequestFormService furnitureRequestFormService;
    public CharityEvent findById(Integer eventId) throws NotFoundException {
        Optional<CharityEvent> charityEvent = charityEventRepository.findById(eventId);
        if (!charityEvent.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        return charityEvent.get();
    }
    public CharityRequest create(CharityRequest charityEventRequest) {
        CharityEvent charityEvent = toEntity(charityEventRequest);
        charityEvent.setCreatedId(String.valueOf(getCurrentUserId()));
        charityEvent=charityEventRepository.save(charityEvent);
        return toCharityRequestDto(charityEvent);
    }
    public CharityRequest update(CharityRequest charityEvent) throws NotFoundException {
        CharityEvent charityEvent1 = toEntity(charityEvent);
        this.charityEventRepository.save(charityEvent1);
        return toCharityRequestDto(charityEvent1);
    }
    public void deleteById(Integer eventId) throws NotFoundException {
        if (!charityEventRepository.findById(eventId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        charityEventRepository.deleteById(eventId);
    }
    public PageInfo<CharityEventDetailDto> viewEventByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<CharityEvent> charityEventPage = charityEventRepository.findByOrderByDateOfEventAsc(pageRequest);
        if (charityEventPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        List<CharityEventDetailDto> charityEventRequests = charityEventPage.getContent().stream().map(charityEvent -> toDto(charityEvent)).collect(Collectors.toList());
        PageInfo<CharityEventDetailDto> eventDtoPageInfo = new PageInfo<>();
        eventDtoPageInfo.setPage(page);
        eventDtoPageInfo.setLimit(limit);
        eventDtoPageInfo.setResult(charityEventRequests);
        eventDtoPageInfo.setTotal(charityEventPage.getTotalElements());
        eventDtoPageInfo.setPages(charityEventPage.getTotalPages());
        return eventDtoPageInfo;
    }
    public CharityEventDetailDto viewEventDetail(Integer id) throws NotFoundException {
        CharityEvent charityEvent = findById(id);
        return toDto(charityEvent);
    }

//    public BenefactorListRequest createListBenefactorForCharity(BenefactorListRequest benefactorListRequest){
//        List<BenefactorCharity> benefactorCharities=toEntity(benefactorListRequest);
//        this.benefactorCharityRepository.saveAll(benefactorCharities);
//        return benefactorListRequest;
//    }

    //mapper
    private CharityEvent toEntity(CharityRequest charityRequest) {
        CharityEvent charityEvent = new CharityEvent();
        charityEvent.setId(charityRequest.getId());
        charityEvent.setDateStart(OrphanUtils.StringToDateTime(charityRequest.getDateStart()));
        charityEvent.setDateEnd(OrphanUtils.StringToDateTime(charityRequest.getDateEnd()));
        charityEvent.setNameCharity(charityRequest.getCharityName());
        charityEvent.setContent(charityRequest.getContent());
        charityEvent.setImage(charityRequest.getImage());
        charityEvent.setTitle(charityRequest.getTitle());
        charityEvent.setIsCompleted(charityRequest.getIsCompleted());
        return charityEvent;
    }
    public CharityRequest toCharityRequestDto(CharityEvent charityEvent){
        CharityRequest charityRequest = new CharityRequest();
        charityRequest.setId(charityEvent.getId());
        charityRequest.setDateStart(OrphanUtils.DateTimeToString(charityEvent.getDateStart()));
        charityRequest.setDateEnd(OrphanUtils.DateTimeToString(charityEvent.getDateEnd()));
        charityRequest.setCharityName(charityEvent.getNameCharity());
        charityRequest.setContent(charityEvent.getContent());
        charityRequest.setImage(charityEvent.getImage());
        charityRequest.setTitle(charityEvent.getTitle());
        return charityRequest;
    }

    private CharityEventDetailDto toDto(CharityEvent charityEvent) {
        CharityEventDetailDto charityEventDetailDto = new CharityEventDetailDto();
        charityEventDetailDto.setId(charityEvent.getId());
        charityEventDetailDto.setTotalDonation(charityEventDetailDto.getTotalDonation());
        charityEventDetailDto.setDateStart(OrphanUtils.DateTimeToString(charityEvent.getDateStart()));
        charityEventDetailDto.setDateEnd(OrphanUtils.DateTimeToString(charityEvent.getDateEnd()));
        charityEventDetailDto.setImage(charityEvent.getImage());
        charityEventDetailDto.setTitle(charityEvent.getTitle());
        charityEventDetailDto.setContent(charityEvent.getContent());
        charityEventDetailDto.setCharityName(charityEvent.getNameCharity());
        charityEventDetailDto.setIsCompleted(charityEvent.getIsCompleted());
        List<BenefactorCharity> benefactorCharities = charityEvent.getBenefactorCharities();
        List<BenefactorCharityRequest> benefactorCharityRequestList;
        benefactorCharityRequestList = benefactorCharities.stream().map(
                benefactorCharity -> toDto(benefactorCharity)
        ).collect(Collectors.toList());
        Long totalDonation = benefactorCharities.stream().mapToLong(benefactorCharity -> benefactorCharity.getDonation()).sum();
        charityEventDetailDto.setBenefactorCharityRequestList(benefactorCharityRequestList);
        charityEventDetailDto.setTotalDonation(totalDonation);
        return charityEventDetailDto;
    }

    private BenefactorCharityRequest toDto(BenefactorCharity benefactorCharity) {
        BenefactorCharityRequest benefactorCharityRequest = new BenefactorCharityRequest();
        benefactorCharityRequest.setId(benefactorCharity.getBenefactorEventId());
        benefactorCharityRequest.setCharityEventId(benefactorCharity.getCharityEvent().getId());
        benefactorCharityRequest.setBenefactorId(benefactorCharity.getBenefactor().getBenefactorId());
        benefactorCharityRequest.setContent(benefactorCharity.getContent());
        benefactorCharityRequest.setDonation(benefactorCharity.getDonation());
        return benefactorCharityRequest;
    }

//    private BenefactorCharity toEntity(BenefactorRequest benefactorRequest,Integer charityEventId){
//        Benefactor benefactor=benefactorRepository.findByPhone(benefactorRequest.getPhone()).orElse(new Benefactor());
//        benefactor.setAddress(benefactorRequest.getAddress());
//        benefactor.setBenefactorId(benefactorRequest.getId());
//        benefactor.setFullName(benefactorRequest.getFullName());
//        benefactor.setPhone(benefactorRequest.getPhone());
//        this.benefactorRepository.save(benefactor);
//        BenefactorCharity benefactorCharity=new BenefactorCharity();
//        benefactorCharity.setBenefactor(benefactor);
//        benefactorCharity.setCharityEvent(charityEventRepository.findById(charityEventId).get());
//        benefactorCharity.setDonation(benefactorRequest.getDonation());
//        benefactorCharity.setContent(benefactorRequest.getContent());
//        List<BenefactorCharity> benefactorCharities=new ArrayList<>();
//        benefactorCharities.add(benefactorCharity
//        );
//        benefactor.setBenefactorCharities(benefactorCharities);
//        this.benefactorRepository.save(benefactor);
//
//        return benefactorCharity;
//    }
//    private List<BenefactorCharity> toEntity(BenefactorListRequest benefactorListRequest){
//        List<BenefactorCharity> benefactorCharities=benefactorListRequest.getBenefactorRequestList()
//                .stream().map(benefactorRequest -> toEntity(benefactorRequest,benefactorListRequest.getCharityEventId())).collect(Collectors.toList());
//        return benefactorCharities;
//    }

}
