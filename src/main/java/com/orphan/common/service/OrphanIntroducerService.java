package com.orphan.common.service;

import com.orphan.api.controller.manager.Children.ChildrenCommonDto;
import com.orphan.api.controller.manager.Children.CommonChildrenService;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerDetailDto;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerDto;
import com.orphan.api.controller.manager.Children.introducer.dto.IntroducerRequest;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.repository.ChildrenRepository;
import com.orphan.common.repository.OrphanIntroducerRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrphanIntroducerService extends BaseService {

    private final OrphanIntroducerRepository orphanIntroducerRepository;

    private final ChildrenRepository childrenRepository;

    private final MessageService messageService;

    private final Logger log = LoggerFactory.getLogger(OrphanIntroducerService.class);

    public OrphanIntroducer findById(Integer introducerId) throws NotFoundException {
        Optional<OrphanIntroducer> introducer = orphanIntroducerRepository.findById(introducerId);
        if (!introducer.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_INTRODUCER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.INTRODUCER));
        }
        return introducer.get();
    }

    public IntroducerRequest createIntroducer(IntroducerRequest introducerRequest) {
        OrphanIntroducer orphanIntroducer = new OrphanIntroducer();

        if (!orphanIntroducerRepository.existsByIdentification(introducerRequest.getIdentification())) {
            orphanIntroducer.setIdentification(introducerRequest.getIdentification());
        }

        if (!orphanIntroducerRepository.existsByEmail(introducerRequest.getEmail())) {
            orphanIntroducer.setEmail(introducerRequest.getEmail());
        }
        orphanIntroducer.setPhone(introducerRequest.getPhone());

        orphanIntroducer.setGender(introducerRequest.getGender());

        if(introducerRequest.getImage()!=null && introducerRequest.getImage()!="") {
            orphanIntroducer.setImage(introducerRequest.getImage());
        }

        orphanIntroducer.setAddress(introducerRequest.getAddress());

        orphanIntroducer.setFullName(introducerRequest.getFullName());

        orphanIntroducer.setDateOfBirth(OrphanUtils.StringToDate(introducerRequest.getDateOfBirth()));

        orphanIntroducer.setCreatedId(String.valueOf(getCurrentUserId()));
        this.orphanIntroducerRepository.save(orphanIntroducer);

        introducerRequest.setId(orphanIntroducer.getIntroducerId());

        return introducerRequest;
    }

    public IntroducerRequest updateIntroducer(IntroducerRequest introducerRequest, Integer introducerId) throws NotFoundException, BadRequestException {
        OrphanIntroducer orphanIntroducer = findById(introducerId);

        if (!orphanIntroducer.getEmail().equals(introducerRequest.getEmail())) {
            if (orphanIntroducerRepository.existsByEmail(introducerRequest.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            orphanIntroducer.setEmail(introducerRequest.getEmail());
        }

        if (!orphanIntroducer.getIdentification().equals(introducerRequest.getIdentification())) {
            if (orphanIntroducerRepository.existsByIdentification(introducerRequest.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            orphanIntroducer.setIdentification(introducerRequest.getIdentification());
        }

        orphanIntroducer.setPhone(introducerRequest.getPhone());

        orphanIntroducer.setGender(introducerRequest.getGender());

        if (introducerRequest.getImage() != "") {
            orphanIntroducer.setImage(introducerRequest.getImage());
        }

        orphanIntroducer.setAddress(introducerRequest.getAddress());

        orphanIntroducer.setFullName(introducerRequest.getFullName());

        orphanIntroducer.setDateOfBirth(OrphanUtils.StringToDate(introducerRequest.getDateOfBirth()));

        orphanIntroducer.setModifiedId(String.valueOf(getCurrentUserId()));

        this.orphanIntroducerRepository.save(orphanIntroducer);

        introducerRequest.setId(introducerId);

        return introducerRequest;
    }

    public IntroducerDetailDto viewIntroducerDetail(Integer introducerId) throws NotFoundException {
        OrphanIntroducer orphanIntroducer = findById(introducerId);
        return OrphanIntroducerToIntroducerDetailDto(orphanIntroducer);
    }

    //View By Page
    public PageInfo<IntroducerDto> viewIntroducersByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("introducerId").ascending());
        Page<OrphanIntroducer> introducerPage = orphanIntroducerRepository.findByOrderByCreatedAtAsc(pageRequest);
        if (introducerPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_INTRODUCER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.INTRODUCER));
        }
        List<IntroducerDto> introducerDtoList = introducerPage.getContent().stream().map(introducer -> OrphanIntroducerToIntroducerDto(introducer)).collect(Collectors.toList());
        PageInfo<IntroducerDto> introducerDtoPageInfo = new PageInfo<>();
        introducerDtoPageInfo.setPage(page);
        introducerDtoPageInfo.setLimit(limit);
        introducerDtoPageInfo.setResult(introducerDtoList);
        introducerDtoPageInfo.setTotal(introducerPage.getTotalElements());
        introducerDtoPageInfo.setPages(introducerPage.getTotalPages());
        return introducerDtoPageInfo;
    }

    //View All
    public List<IntroducerDto> viewAllIntroducers() throws NotFoundException {
        List<OrphanIntroducer> orphanIntroducerList = orphanIntroducerRepository.findAll();
        if (orphanIntroducerList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        List<IntroducerDto> introducerDtoList = orphanIntroducerList.stream().map(introducer -> OrphanIntroducerToIntroducerDto(introducer)).collect(Collectors.toList());
        return introducerDtoList;
    }

    public void deleteIntroducer(Integer introduceId) throws NotFoundException {
        childrenRepository.updateOrphanIntroducerByOrphanIntroducer_IntroducerId(null, introduceId);
        if (orphanIntroducerRepository.existsById(introduceId)) {
            orphanIntroducerRepository.deleteById(introduceId);
        } else {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_INTRODUCER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.INTRODUCER));
        }

    }

    public PageInfo<IntroducerDto> searchIntroducer(String keyword, Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<OrphanIntroducer> introducerPage=null;

        introducerPage = orphanIntroducerRepository.searchIntroducer(keyword, pageRequest);
        if (introducerPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_INTRODUCER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.INTRODUCER));
        }
        List<IntroducerDto> introducerDtoList = introducerPage.getContent().stream().map(introducer -> OrphanIntroducerToIntroducerDto(introducer)).collect(Collectors.toList());
        PageInfo<IntroducerDto> introducerDtoPageInfo = new PageInfo<>();
        introducerDtoPageInfo.setPage(page);
        introducerDtoPageInfo.setLimit(limit);
        introducerDtoPageInfo.setResult(introducerDtoList);
        introducerDtoPageInfo.setTotal(introducerPage.getTotalElements());
        introducerDtoPageInfo.setPages(introducerPage.getTotalPages());
        return introducerDtoPageInfo;
    }

    //mapper
    private IntroducerDetailDto OrphanIntroducerToIntroducerDetailDto(OrphanIntroducer orphanIntroducer) {
        IntroducerDetailDto introducerDetailDto = new IntroducerDetailDto();
        introducerDetailDto.setId(orphanIntroducer.getIntroducerId());
        introducerDetailDto.setEmail(orphanIntroducer.getEmail());
        introducerDetailDto.setAddress(orphanIntroducer.getAddress());
        introducerDetailDto.setImage(orphanIntroducer.getImage());
        introducerDetailDto.setIdentification(orphanIntroducer.getIdentification());
        introducerDetailDto.setGender(orphanIntroducer.getGender());
        introducerDetailDto.setDateOfBirth(OrphanUtils.DateToString(orphanIntroducer.getDateOfBirth()));
        introducerDetailDto.setFullName(orphanIntroducer.getFullName());
        introducerDetailDto.setPhone(orphanIntroducer.getPhone());
        List<ChildrenCommonDto> childrenCommonDtoList = new ArrayList<ChildrenCommonDto>();
        introducerDetailDto.setChildrens(orphanIntroducer.getChildrens().stream()
                .map(children -> CommonChildrenService.childrenToChildrenCommonDto(children))
                .collect(Collectors.toList()));
        return introducerDetailDto;
    }

    private IntroducerDto OrphanIntroducerToIntroducerDto(OrphanIntroducer orphanIntroducer) {
        IntroducerDto introducerDto = new IntroducerDto();
        introducerDto.setId(orphanIntroducer.getIntroducerId());
        introducerDto.setAddress(orphanIntroducer.getAddress());
        introducerDto.setImage(orphanIntroducer.getImage());
        introducerDto.setPhone(orphanIntroducer.getPhone());
        introducerDto.setGender(orphanIntroducer.getGender());
        introducerDto.setChildrens(orphanIntroducer.getChildrens().stream()
                .map(children -> CommonChildrenService.childrenToChildrenCommonDto(children))
                .collect(Collectors.toList()));
        introducerDto.setFullName(orphanIntroducer.getFullName());
        return introducerDto;
    }


}
