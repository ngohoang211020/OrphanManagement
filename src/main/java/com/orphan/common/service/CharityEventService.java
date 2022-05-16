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

    public BenefactorListRequest createListBenefactorForCharity(BenefactorListRequest benefactorListRequest) {
        List<Benefactor> benefactors=benefactorListRequest.getBenefactorRequestList().stream().map(
                benefactorRequest -> toEntity(benefactorRequest,benefactorListRequest.getCharityEventId())
        ).collect(Collectors.toList());
        benefactorRepository.saveAll(benefactors);


//        List<BenefactorCharity> benefactorCharities;
//        benefactorCharities = benefactorListRequest.getBenefactorRequestList().stream().map(
//                benefactorCharity -> toEntity1(benefactorCharity)
//        ).collect(Collectors.toList());
//        benefactorCharityRepository.saveAll(benefactorCharities);
//        CharityEvent charityEvent=charityEventRepository.findById(benefactorListRequest.getCharityEventId()).get();
//        charityEvent.setBenefactorCharities(benefactorCharities);
//        this.charityEventRepository.save(charityEvent);
        return benefactorListRequest;
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

    //mapper
    private CharityEvent toEntity(CharityRequest charityRequest) {
        CharityEvent charityEvent = new CharityEvent();
        charityEvent.setCharityEventId(charityRequest.getId());
        charityEvent.setDateOfEvent(OrphanUtils.StringToDate(charityRequest.getDateOfEvent()));
        charityEvent.setCharityName(charityRequest.getCharityName());
        charityEvent.setContent(charityRequest.getContent());
        charityEvent.setImage(charityRequest.getImage());
        charityEvent.setTitle(charityRequest.getTitle());
        return charityEvent;
    }
    public CharityRequest toCharityRequestDto(CharityEvent charityEvent){
        CharityRequest charityRequest = new CharityRequest();
        charityRequest.setId(charityEvent.getCharityEventId());
        charityRequest.setDateOfEvent(OrphanUtils.DateToString(charityEvent.getDateOfEvent()));
        charityRequest.setCharityName(charityEvent.getCharityName());
        charityRequest.setContent(charityEvent.getContent());
        charityRequest.setImage(charityEvent.getImage());
        charityRequest.setTitle(charityEvent.getTitle());
        return charityRequest;
    }

    private CharityEventDetailDto toDto(CharityEvent charityEvent) {
        CharityEventDetailDto charityEventDetailDto = new CharityEventDetailDto();
        charityEventDetailDto.setId(charityEvent.getCharityEventId());
        charityEventDetailDto.setTotalDonation(charityEventDetailDto.getTotalDonation());
        charityEventDetailDto.setDateOfEvent(OrphanUtils.DateToString(charityEvent.getDateOfEvent()));
        charityEventDetailDto.setImage(charityEvent.getImage());
        charityEventDetailDto.setTitle(charityEvent.getTitle());
        charityEventDetailDto.setContent(charityEvent.getContent());
        charityEventDetailDto.setStatus(charityEvent.getStatus());
        List<BenefactorCharity> benefactorCharities = charityEvent.getBenefactorCharities();
        List<BenefactorCharityRequest> benefactorCharityRequestList = new ArrayList<>();
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
        benefactorCharityRequest.setCharityEventId(benefactorCharity.getCharityEvent().getCharityEventId());
        benefactorCharityRequest.setBenefactorId(benefactorCharity.getBenefactor().getBenefactorId());
        benefactorCharityRequest.setContent(benefactorCharity.getContent());
        benefactorCharityRequest.setDonation(benefactorCharity.getDonation());
        return benefactorCharityRequest;
    }



    private Benefactor toEntity(BenefactorRequest benefactorRequest,Integer charityEventId) {
        Benefactor benefactor = benefactorRepository.findByPhone( benefactorRequest.getPhone()).orElse(null);
        if(benefactor==null) {
            benefactor.setPhone(benefactorRequest.getPhone());
            benefactor.setAddress(benefactorRequest.getAddress());
            benefactor.setFullName(benefactorRequest.getFullName());
            this.benefactorRepository.save(benefactor);
        }
//        BenefactorCharityRequest benefactorCharityRequest = new BenefactorCharityRequest();
//        benefactorCharityRequest.setDonation(benefactorRequest.getDonation());
//        benefactorCharityRequest.setBenefactorId(benefactorRequest.getId());
//        benefactorCharityRequest.setContent(benefactorRequest.getContent());
//        benefactorCharityRequest.setCharityEventId(charityEventId);
//        benefactorCharityRepository.save(toEntity(benefactorCharityRequest));
//        List<BenefactorCharity> benefactorCharityList = new ArrayList<>();
//        benefactorCharityList.add(toEntity(benefactorCharityRequest));
//        benefactor.setBenefactorCharities(benefactorCharityList);
//        benefactor.setTotalDonation(benefactor.getTotalDonation() + benefactorRequest.getDonation());
        return benefactor;
    }
//
//    private BenefactorCharity toEntity1(BenefactorRequest benefactorRequest) {
//        BenefactorCharity benefactorCharity = new BenefactorCharity();
//        Benefactor benefactor = toEntity(benefactorRequest);
//        benefactorRepository.save(benefactor);
//        CharityEvent charityEvent = charityEventRepository.findById(benefactorRequest.getCharityEventId()).get();
//        benefactorCharity.setDonation(benefactorRequest.getDonation());
//        benefactorCharity.setContent(benefactorRequest.getContent());
//        benefactorCharity.setBenefactor(benefactor);
//        benefactorCharity.setCharityEvent(charityEvent);
//        return benefactorCharity;
//    }
//
//
    private BenefactorCharity toEntity(BenefactorCharityRequest benefactorCharityRequest) {
        BenefactorCharity benefactorCharity = new BenefactorCharity();
        Benefactor benefactor = benefactorRepository.findById(benefactorCharityRequest.getBenefactorId()).get();
        CharityEvent charityEvent = charityEventRepository.findById(benefactorCharityRequest.getCharityEventId()).get();
        benefactorCharity.setBenefactor(benefactor);
        benefactorCharity.setCharityEvent(charityEvent);
        benefactorCharity.setDonation(benefactorCharityRequest.getDonation());
        benefactorCharity.setContent(benefactorCharityRequest.getContent());
        return benefactorCharity;
    }
//
//    private BenefactorRequest toDto(Benefactor benefactor) {
//        BenefactorRequest benefactorRequest = new BenefactorRequest();
//        benefactorRequest.setId(benefactor.getBenefactorId());
//        benefactorRequest.setAddress(benefactor.getAddress());
//        benefactorRequest.setPhone(benefactor.getPhone());
//        benefactorRequest.setFullName(benefactor.getFullName());
//        Long donation = benefactor.getBenefactorCharities().stream().mapToLong(benefactorCharity -> benefactorCharity.getDonation()).sum();
//        benefactorRequest.setDonation(donation);
//        return benefactorRequest;
//    }
}
