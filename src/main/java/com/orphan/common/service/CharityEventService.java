package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.EventRequest;
import com.orphan.api.controller.manager.Logistic.CharityEvent.dto.EventDto;
import com.orphan.common.entity.CharityEvent;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class CharityEventService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(CharityEventService.class);

    private final CharityEventRepository charityEventRepository;

    public CharityEvent findById(Integer eventId) throws NotFoundException {
        Optional<CharityEvent> charityEvent = charityEventRepository.findById(eventId);
        if (!charityEvent.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        return charityEvent.get();
    }

    public EventRequest createCharityEvent(EventRequest eventRequest) {
        CharityEvent charityEvent = toEntity(eventRequest) ;

        charityEvent.setCreatedId(String.valueOf(getCurrentUserId()));

        charityEvent=this.charityEventRepository.save(charityEvent);

        eventRequest.setIdEvent(charityEvent.getEventId());

        return eventRequest;
    }


    public EventRequest updateCharityEvent(EventRequest eventRequest, Integer eventId) throws NotFoundException {

        CharityEvent charityEvent = findById(eventId);

        charityEvent.setNameEvent(eventRequest.getNameEvent());
        charityEvent.setDonors(eventRequest.getDonors());

        charityEvent.setDateOfEvent(OrphanUtils.StringToDate(eventRequest.getDateOfEvent()));


        if(eventRequest.getImage()!=""){
            charityEvent.setNameEvent(eventRequest.getImage());
            eventRequest.setImage(charityEvent.getImage());
        }
        this.charityEventRepository.save(charityEvent);

        charityEvent.setMoney(eventRequest.getMoney());
        charityEvent.setQuantity(eventRequest.getQuantity());
        charityEvent.setTimeOfEvent(eventRequest.getTimeOfEvent());



        eventRequest.setIdEvent(eventId);

        return eventRequest;
    }

    public void deleteById(Integer eventId) throws NotFoundException {
        if (!charityEventRepository.findById(eventId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        charityEventRepository.deleteById(eventId);
    }


    public PageInfo<EventDto> viewEventByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest=buildPageRequest(page,limit);
        Page<CharityEvent> charityEventPage = charityEventRepository.findByOrderByNameEventAsc(pageRequest);
        if (charityEventPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        List<EventDto> eventDtoList = charityEventPage.getContent().stream().map(charityEvent -> toEntityDto(charityEvent)).collect(Collectors.toList());
        PageInfo<EventDto> eventDtoPageInfo=new PageInfo<>();
        eventDtoPageInfo.setPage(page);
        eventDtoPageInfo.setLimit(limit);
        eventDtoPageInfo.setResult(eventDtoList);
        eventDtoPageInfo.setTotal(charityEventPage.getTotalElements());
        eventDtoPageInfo.setPages(charityEventPage.getTotalPages());
        return eventDtoPageInfo;
    }

    public List<EventDto> viewAllEvent() throws NotFoundException {
        List<CharityEvent> charityEventList = charityEventRepository.findAll();
        if (charityEventList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_EVENT_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EVENT));
        }
        List<EventDto> eventDtoList = charityEventList.stream().map(charityEvent -> toEntityDto(charityEvent)).collect(Collectors.toList());
        return  eventDtoList;
    }

    public EventDto viewEventDetail(Integer eventId) throws NotFoundException {
        CharityEvent charityEvent = findById(eventId);
        return toEntityDto(charityEvent);
    }

    //mapper
    private CharityEvent toEntity(EventRequest eventRequest){
        CharityEvent charityEvent=new CharityEvent();
        charityEvent.setEventId(eventRequest.getIdEvent());
        charityEvent.setImage(eventRequest.getImage());
        charityEvent.setQuantity(eventRequest.getQuantity());
        charityEvent.setNameEvent(eventRequest.getNameEvent());
        charityEvent.setDateOfEvent(OrphanUtils.StringToDate(eventRequest.getDateOfEvent()));
        charityEvent.setDonors(eventRequest.getDonors());
        charityEvent.setMoney(eventRequest.getMoney());
        charityEvent.setTimeOfEvent(eventRequest.getTimeOfEvent());
       return charityEvent;
    }

    private EventDto toEntityDto(CharityEvent charityEvent){
        EventDto eventDto=new EventDto();
        eventDto.setIdEvent(charityEvent.getEventId());
        eventDto.setImage(charityEvent.getImage());
        eventDto.setQuantity(charityEvent.getQuantity());
        eventDto.setNameEvent(charityEvent.getNameEvent());
        eventDto.setDateOfEvent(OrphanUtils.DateToString(charityEvent.getDateOfEvent()));
        eventDto.setDonors(charityEvent.getDonors());
        eventDto.setMoney(charityEvent.getMoney());
        eventDto.setTimeOfEvent(charityEvent.getTimeOfEvent());
        return eventDto;
    }

}
