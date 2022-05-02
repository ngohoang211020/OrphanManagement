package com.orphan.common.service;

import com.orphan.api.controller.manager.Children.ChildrenCommonDto;
import com.orphan.api.controller.manager.Children.CommonChildrenService;
import com.orphan.api.controller.manager.Children.nurturer.dto.NurturerDetailDto;
import com.orphan.api.controller.manager.Children.nurturer.dto.NurturerDto;
import com.orphan.api.controller.manager.Children.nurturer.dto.NurturerRequest;
import com.orphan.common.entity.OrphanNurturer;
import com.orphan.common.repository.ChildrenRepository;
import com.orphan.common.repository.OrphanNurturerRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrphanNurturerService extends BaseService {

    private final OrphanNurturerRepository orphanNurturerRepository;

    private final ChildrenRepository childrenRepository;

    private final MessageService messageService;

    private final Logger log = LoggerFactory.getLogger(OrphanNurturerRepository.class);

    public OrphanNurturer findById(Integer nurturerId) throws NotFoundException {
        Optional<OrphanNurturer> nurturer = orphanNurturerRepository.findById(nurturerId);
        if (!nurturer.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_NURTURER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.NURTURER));
        }
        return nurturer.get();
    }

    public NurturerRequest createNurturer(NurturerRequest nurturerRequest) {
        OrphanNurturer orphanNurturer = new OrphanNurturer();

        if (!orphanNurturerRepository.existsByIdentification(nurturerRequest.getIdentification())) {
            orphanNurturer.setIdentification(nurturerRequest.getIdentification());
        }

        if (!orphanNurturerRepository.existsByEmail(nurturerRequest.getEmail())) {
            orphanNurturer.setEmail(nurturerRequest.getEmail());
        }
        orphanNurturer.setPhone(nurturerRequest.getPhone());

        orphanNurturer.setGender(nurturerRequest.getGender());

        if (nurturerRequest.getImage() != "" && nurturerRequest.getImage() != null) {
            orphanNurturer.setImage(nurturerRequest.getImage());
        }


        orphanNurturer.setAddress(nurturerRequest.getAddress());

        orphanNurturer.setFullName(nurturerRequest.getFullName());

        orphanNurturer.setDateOfBirth(OrphanUtils.StringToDate(nurturerRequest.getDateOfBirth()));

        orphanNurturer.setCreatedId(String.valueOf(getCurrentUserId()));
        this.orphanNurturerRepository.save(orphanNurturer);

        nurturerRequest.setId(orphanNurturer.getNurturerId());

        return nurturerRequest;
    }

    public NurturerRequest updateNurturer(NurturerRequest nurturerRequest, Integer nurturerId) throws NotFoundException, BadRequestException {
        OrphanNurturer orphanNurturer = findById(nurturerId);

        if (!orphanNurturer.getEmail().equals(nurturerRequest.getEmail())) {
            if (orphanNurturerRepository.existsByEmail(nurturerRequest.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            orphanNurturer.setEmail(nurturerRequest.getEmail());
        }

        if (!orphanNurturer.getIdentification().equals(nurturerRequest.getIdentification())) {
            if (orphanNurturerRepository.existsByIdentification(nurturerRequest.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            orphanNurturer.setIdentification(nurturerRequest.getIdentification());
        }

        orphanNurturer.setPhone(nurturerRequest.getPhone());

        orphanNurturer.setGender(nurturerRequest.getGender());

        if (nurturerRequest.getImage() != "") {
            orphanNurturer.setImage(nurturerRequest.getImage());
        }

        orphanNurturer.setAddress(nurturerRequest.getAddress());

        orphanNurturer.setFullName(nurturerRequest.getFullName());

        orphanNurturer.setDateOfBirth(OrphanUtils.StringToDate(nurturerRequest.getDateOfBirth()));

        orphanNurturer.setModifiedId(String.valueOf(getCurrentUserId()));

        this.orphanNurturerRepository.save(orphanNurturer);

        nurturerRequest.setId(nurturerId);

        return nurturerRequest;
    }

    public NurturerDetailDto viewNurturerDetail(Integer nurturer) throws NotFoundException {
        OrphanNurturer orphanNurturer = findById(nurturer);
        return OrphanNurturerToNurturerDetailDto(orphanNurturer);
    }

    //View By Page
    public PageInfo<NurturerDto> viewNurturersByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<OrphanNurturer> nurturerPage = orphanNurturerRepository.findByOrderByFullNameAsc(pageRequest);
        if (nurturerPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_NURTURER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.NURTURER));
        }
        List<NurturerDto> nurturerDtoList = nurturerPage.getContent().stream().map(nurturer -> OrphanNurturerToNurturerDto(nurturer)).collect(Collectors.toList());
        PageInfo<NurturerDto> nurturerDtoPageInfo = new PageInfo<>();
        nurturerDtoPageInfo.setPage(page);
        nurturerDtoPageInfo.setLimit(limit);
        nurturerDtoPageInfo.setResult(nurturerDtoList);
        nurturerDtoPageInfo.setTotal(nurturerPage.getTotalElements());
        nurturerDtoPageInfo.setPages(nurturerPage.getTotalPages());
        return nurturerDtoPageInfo;
    }

    //View All
    public List<NurturerDto> viewAllNurturers() throws NotFoundException {
        List<OrphanNurturer> orphanNurturerList = orphanNurturerRepository.findAll();
        if (orphanNurturerList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_NURTURER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.NURTURER));
        }
        List<NurturerDto> nurturerDtoList = orphanNurturerList.stream().map(nurturer -> OrphanNurturerToNurturerDto(nurturer)).collect(Collectors.toList());
        return nurturerDtoList;
    }

    public void deleteNurturer(Integer nurturerId) throws NotFoundException {
//        childrenRepository.updateStatusAndOrphanNurturerByOrphanNurturer_NurturerId(ChildrenStatus.WAIT_TO_RECEIVE.getCode(), null, nurturerId);
        if (orphanNurturerRepository.existsById(nurturerId)) {
            orphanNurturerRepository.deleteById(nurturerId);
        } else {
            throw new NotFoundException(NotFoundException.ERROR_ORPHAN_NURTURER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.NURTURER));
        }

    }

    //mapper
    private NurturerDetailDto OrphanNurturerToNurturerDetailDto(OrphanNurturer orphanNurturer) {
        NurturerDetailDto nurturerDetailDto = new NurturerDetailDto();
        nurturerDetailDto.setId(orphanNurturer.getNurturerId());
        nurturerDetailDto.setEmail(orphanNurturer.getEmail());
        nurturerDetailDto.setAddress(orphanNurturer.getAddress());
        nurturerDetailDto.setImage(orphanNurturer.getImage());
        nurturerDetailDto.setIdentification(orphanNurturer.getIdentification());
        nurturerDetailDto.setGender(orphanNurturer.getGender());
        nurturerDetailDto.setDateOfBirth(OrphanUtils.DateToString(orphanNurturer.getDateOfBirth()));
        nurturerDetailDto.setFullName(orphanNurturer.getFullName());
        nurturerDetailDto.setPhone(orphanNurturer.getPhone());
        List<ChildrenCommonDto> childrenCommonDtoList = new ArrayList<ChildrenCommonDto>();
        nurturerDetailDto.setChildrens(orphanNurturer.getChildrens().stream()
                .map(children -> CommonChildrenService.childrenToChildrenCommonDto(children))
                .collect(Collectors.toList()));
        return nurturerDetailDto;
    }

    private NurturerDto OrphanNurturerToNurturerDto(OrphanNurturer orphanNurturer) {
        NurturerDto nurturerDto = new NurturerDto();
        nurturerDto.setId(orphanNurturer.getNurturerId());
        nurturerDto.setAddress(orphanNurturer.getAddress());
        nurturerDto.setImage(orphanNurturer.getImage());
        nurturerDto.setPhone(orphanNurturer.getPhone());
        nurturerDto.setGender(orphanNurturer.getGender());
        nurturerDto.setChildrens(orphanNurturer.getChildrens().stream()
                .map(children -> CommonChildrenService.childrenToChildrenCommonDto(children))
                .collect(Collectors.toList()));
        nurturerDto.setFullName(orphanNurturer.getFullName());
        return nurturerDto;
    }


}
