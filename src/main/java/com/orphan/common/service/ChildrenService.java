package com.orphan.common.service;

import com.orphan.api.controller.manager.Children.children.dto.ChildrenDetailDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenRequest;
import com.orphan.common.entity.Children;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import com.orphan.common.repository.ChildrenRepository;
import com.orphan.common.repository.OrphanIntroducerRepository;
import com.orphan.common.repository.OrphanNurturerRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.ChildrenStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
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
public class ChildrenService extends BaseService {

    private final ChildrenRepository childrenRepository;

    private final OrphanIntroducerRepository orphanIntroducerRepository;

    private final OrphanNurturerRepository orphanNuturerRepository;

    public Children findById(Integer childrenId) throws NotFoundException {
        Optional<Children> childrenOptional = childrenRepository.findById(childrenId);
        if (!childrenOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        return childrenOptional.get();
    }

    public ChildrenRequest createChildren(ChildrenRequest childrenRequest) throws NotFoundException {
        Children children = ChildrenRequestToChidren(childrenRequest);
        children.setCreatedId(String.valueOf(getCurrentUserId()));
        this.childrenRepository.save(children);
        childrenRequest.setId(children.getId());
        return childrenRequest;
    }

    public ChildrenRequest updateChildrenDetail(ChildrenRequest childrenRequest, Integer childrenId) throws NotFoundException {
        Children children = findById(childrenId);
        children.setGender(childrenRequest.getGender());
        children.setDateOfBirth(OrphanUtils.StringToDate(childrenRequest.getDateOfBirth()));
        children.setFullName(childrenRequest.getFullName());
        children.setAdoptiveDate(OrphanUtils.StringToDate(childrenRequest.getAdoptiveDate()));

        if(childrenRequest.getDateReceivedOfNurturer()!=""){
            children.setDateReceivedOfNurturer(OrphanUtils.StringToDate(childrenRequest.getDateReceivedOfNurturer()));
            OrphanNurturer orphanNurturer=orphanNuturerRepository.findById(childrenRequest.getNurturerId()).get();
            children.setStatus(ChildrenStatus.RECEIVED.getCode());
            children.setOrphanNurturer(orphanNurturer);
        }
        else{
            children.setStatus(ChildrenStatus.WAIT_TO_RECEIVE.getCode());
        }
        if(childrenRequest.getIntroducerId()!=0){
            OrphanIntroducer orphanIntroducer=orphanIntroducerRepository.findById(childrenRequest.getIntroducerId()).get();
            children.setOrphanIntroducer(orphanIntroducer);
        }
        if(childrenRequest.getImage()!=""){
            children.setImage(childrenRequest.getImage());
        }
        children.setModifiedId(String.valueOf(getCurrentUserId()));
        this.childrenRepository.save(children);
        childrenRequest.setId(childrenId);
        return childrenRequest;
    }

    public ChildrenDetailDto viewChildrenDetail(Integer childrenId) throws NotFoundException {
        Children children = findById(childrenId);
        return ChildrenToChildrenDetailDto(children);
    }

    public List<ChildrenDto> viewAllChildrens() throws NotFoundException {
        List<Children> childrenList = childrenRepository.findAll();
        if (childrenList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        List<ChildrenDto> childrenDtoList = childrenList.stream().map(children -> {
            return ChildrenToChildrenDto(children);
        }).collect(Collectors.toList());
        return childrenDtoList;
    }

    public PageInfo<ChildrenDto> viewChildrensByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<Children> childrenPage = childrenRepository.findByOrderByFullNameAsc(pageRequest);
        if (childrenPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        List<ChildrenDto> childrenDtoList = childrenPage.getContent().stream().map(children -> {
            return ChildrenToChildrenDto(children);
        }).collect(Collectors.toList());
        PageInfo<ChildrenDto> childrenDtoPageInfo = new PageInfo<>();
        childrenDtoPageInfo.setPage(page);
        childrenDtoPageInfo.setLimit(limit);
        childrenDtoPageInfo.setResult(childrenDtoList);
        childrenDtoPageInfo.setTotal(childrenPage.getTotalElements());
        childrenDtoPageInfo.setPages(childrenPage.getTotalPages());
        return childrenDtoPageInfo;
    }

    public void deleteById(Integer childrenId) throws NotFoundException {
        if (!childrenRepository.findById(childrenId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        childrenRepository.deleteById(childrenId);
    }


    //mapper

    private ChildrenDto ChildrenToChildrenDto(Children children) {
        ChildrenDto childrenDto = new ChildrenDto();
        childrenDto.setId(children.getId());
        childrenDto.setFullName(children.getFullName());
        childrenDto.setImage(children.getImage());
        childrenDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenDto.setStatus(children.getStatus());
        return childrenDto;
    }

    private ChildrenDetailDto ChildrenToChildrenDetailDto(Children children) {
        ChildrenDetailDto childrenDetailDto = new ChildrenDetailDto();
        childrenDetailDto.setId(children.getId());
        childrenDetailDto.setFullName(children.getFullName());
        childrenDetailDto.setImage(children.getImage());
        childrenDetailDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenDetailDto.setStatus(children.getStatus());
        childrenDetailDto.setGender(children.getGender());
        childrenDetailDto.setAdoptiveDate(OrphanUtils.DateToString(children.getAdoptiveDate()));

        if(children.getOrphanIntroducer()!=null){
            childrenDetailDto.setNameOfIntroducer(children.getOrphanIntroducer().getFullName());
        }
        if(children.getOrphanNurturer()!=null){
            childrenDetailDto.setDateReceived(OrphanUtils.DateToString(children.getDateReceivedOfNurturer()));
            childrenDetailDto.setNameOfNurturer(children.getOrphanNurturer().getFullName());
        }
        return childrenDetailDto;
    }

    private Children ChildrenRequestToChidren(ChildrenRequest childrenRequest) {
        Children children = new Children();
        children.setGender(childrenRequest.getGender());
        children.setDateOfBirth(OrphanUtils.StringToDate(childrenRequest.getDateOfBirth()));
        children.setFullName(childrenRequest.getFullName());
        children.setAdoptiveDate(OrphanUtils.StringToDate(childrenRequest.getAdoptiveDate()));
        children.setImage(childrenRequest.getImage());
        if(childrenRequest.getDateReceivedOfNurturer()!="" && childrenRequest.getNurturerId()!=null){
            children.setDateReceivedOfNurturer(OrphanUtils.StringToDate(childrenRequest.getDateReceivedOfNurturer()));
            OrphanNurturer orphanNurturer=orphanNuturerRepository.findById(childrenRequest.getNurturerId()).get();
            children.setStatus(ChildrenStatus.RECEIVED.getCode());
            children.setOrphanNurturer(orphanNurturer);
        }
        else{
            children.setStatus(ChildrenStatus.WAIT_TO_RECEIVE.getCode());
        }
        if(childrenRequest.getIntroducerId()!=0 && childrenRequest.getIntroducerId()!=null){
            OrphanIntroducer orphanIntroducer=orphanIntroducerRepository.findById(childrenRequest.getIntroducerId()).get();
            children.setOrphanIntroducer(orphanIntroducer);
        }
        children.setId(childrenRequest.getId());
        return children;
    }


}
